package cn.qihangerp.service;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.mapper.*;
import cn.qihangerp.model.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ErpAfterSaleService {
    @Autowired private ErpAfterSaleMapper mapper;
    @Autowired private ErpAfterSaleItemMapper itemMapper;
    @Autowired private OGoodsSkuStockMapper stockMapper;
    @Autowired private OGoodsSkuStockWarehouseMapper whStockMapper;
    @Autowired private OOrderMapper orderMapper;

    public PageResult<ErpAfterSale> list(String afterNum, String orderNum, Integer type, Integer status, PageQuery pageQuery) {
        var qw = new LambdaQueryWrapper<ErpAfterSale>()
            .like(StringUtils.hasText(afterNum), ErpAfterSale::getAfterNum, afterNum)
            .like(StringUtils.hasText(orderNum), ErpAfterSale::getOrderNum, orderNum)
            .eq(type != null, ErpAfterSale::getType, type)
            .eq(status != null, ErpAfterSale::getStatus, status)
            .orderByDesc(ErpAfterSale::getId);
        IPage<ErpAfterSale> page = mapper.selectPage(pageQuery.build(), qw);
        List<ErpAfterSale> records = page.getRecords();
        if (!records.isEmpty()) {
            List<Long> ids = records.stream().map(ErpAfterSale::getId).toList();
            List<ErpAfterSaleItem> allItems = itemMapper.selectList(
                new LambdaQueryWrapper<ErpAfterSaleItem>().in(ErpAfterSaleItem::getAfterSaleId, ids));
            Map<Long, List<ErpAfterSaleItem>> grouped = allItems.stream()
                .collect(Collectors.groupingBy(ErpAfterSaleItem::getAfterSaleId));
            records.forEach(o -> o.setItemList(grouped.get(o.getId())));
        }
        return PageResult.build(page);
    }

    public ErpAfterSale getById(Long id) {
        ErpAfterSale as = mapper.selectById(id);
        if (as != null) {
            List<ErpAfterSaleItem> items = itemMapper.selectList(
                new LambdaQueryWrapper<ErpAfterSaleItem>().eq(ErpAfterSaleItem::getAfterSaleId, id));
            as.setItemList(items);
        }
        return as;
    }

    @Transactional
    public void create(ErpAfterSale afterSale, List<ErpAfterSaleItem> items) {
        afterSale.setAfterNum("AS" + System.currentTimeMillis());
        afterSale.setCreateTime(new Date());
        mapper.insert(afterSale);

        if (items != null) {
            for (ErpAfterSaleItem item : items) {
                item.setAfterSaleId(afterSale.getId());
                item.setAfterSaleNum(afterSale.getAfterNum());
                item.setCreateTime(new Date());
                itemMapper.insert(item);
            }
        }
    }

    @Transactional
    public void approve(Long id) {
        ErpAfterSale as = mapper.selectById(id);
        if (as == null) throw new RuntimeException("售后单不存在");
        if (as.getStatus() != 0) throw new RuntimeException("当前状态不能审核");
        as.setStatus(1);
        mapper.updateById(as);
    }

    @Transactional
    public void refund(Long id) {
        ErpAfterSale as = mapper.selectById(id);
        if (as == null) throw new RuntimeException("售后单不存在");
        if (as.getStatus() != 1) throw new RuntimeException("请先审核通过");
        as.setStatus(2);
        as.setCompleteTime(new Date());
        mapper.updateById(as);

        OOrder order = orderMapper.selectById(as.getOrderId());
        if (order != null) {
            order.setOrderStatus(4);
            order.setUpdateTime(new Date());
            orderMapper.updateById(order);
        }
    }

    @Transactional
    public void returnStock(Long id, Long warehouseId, String warehouseName) {
        ErpAfterSale as = mapper.selectById(id);
        if (as == null) throw new RuntimeException("售后单不存在");
        if (as.getStatus() != 1) throw new RuntimeException("请先审核通过");
        if (as.getType() != 2) throw new RuntimeException("仅退货退款类型才能退货入库");

        List<ErpAfterSaleItem> items = itemMapper.selectList(
            new LambdaQueryWrapper<ErpAfterSaleItem>().eq(ErpAfterSaleItem::getAfterSaleId, id));

        for (ErpAfterSaleItem item : items) {
            int qty = item.getQuantity() != null ? item.getQuantity() : 0;
            if (qty > 0 && item.getSkuId() != null) {
                OGoodsSkuStock stock = stockMapper.selectOne(
                    new LambdaQueryWrapper<OGoodsSkuStock>().eq(OGoodsSkuStock::getSkuId, item.getSkuId()));
                if (stock == null) {
                    stock = new OGoodsSkuStock();
                    stock.setSkuId(item.getSkuId());
                    stock.setGoodsId(item.getGoodsId());
                    stock.setSkuCode(item.getSkuCode());
                    stock.setGoodsName(item.getGoodsName());
                    stock.setGoodsImg(item.getGoodsImage());
                    stock.setSkuName(item.getSkuName());
                    stock.setQuantity(qty);
                    stock.setCreateTime(new Date());
                    stockMapper.insert(stock);
                } else {
                    stock.setQuantity((stock.getQuantity() != null ? stock.getQuantity() : 0) + qty);
                    stock.setUpdateTime(new Date());
                    stockMapper.updateById(stock);
                }

                if (warehouseId != null) {
                    OGoodsSkuStockWarehouse whStock = whStockMapper.selectOne(
                        new LambdaQueryWrapper<OGoodsSkuStockWarehouse>()
                            .eq(OGoodsSkuStockWarehouse::getWarehouseId, warehouseId)
                            .eq(OGoodsSkuStockWarehouse::getSkuId, item.getSkuId()));
                    if (whStock == null) {
                        whStock = new OGoodsSkuStockWarehouse();
                        whStock.setSkuId(item.getSkuId());
                        whStock.setGoodsId(item.getGoodsId());
                        whStock.setWarehouseId(warehouseId);
                        whStock.setWarehouseName(warehouseName);
                        whStock.setSkuCode(item.getSkuCode());
                        whStock.setGoodsName(item.getGoodsName());
                        whStock.setGoodsImg(item.getGoodsImage());
                        whStock.setSkuName(item.getSkuName());
                        whStock.setQuantity(qty);
                        whStock.setCreateTime(new Date());
                        whStockMapper.insert(whStock);
                    } else {
                        whStock.setQuantity((whStock.getQuantity() != null ? whStock.getQuantity() : 0) + qty);
                        whStock.setUpdateTime(new Date());
                        whStockMapper.updateById(whStock);
                    }
                }

                item.setReturnQuantity(qty);
                item.setUpdateTime(new Date());
                itemMapper.updateById(item);
            }
        }

        as.setStatus(3);
        as.setCompleteTime(new Date());
        mapper.updateById(as);

        OOrder order = orderMapper.selectById(as.getOrderId());
        if (order != null) {
            order.setOrderStatus(4);
            order.setUpdateTime(new Date());
            orderMapper.updateById(order);
        }
    }
}