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

import java.util.Date;
import java.util.List;

@Service
public class ErpStockOutService {
    @Autowired private ErpStockOutMapper mapper;
    @Autowired private ErpStockOutItemMapper itemMapper;
    @Autowired private OOrderMapper orderMapper;
    @Autowired private OGoodsSkuStockMapper stockMapper;
    @Autowired private OGoodsSkuStockWarehouseMapper whStockMapper;

    public PageResult<ErpStockOut> list(String outNum, String sourceNum, Integer status, PageQuery pageQuery) {
        var qw = new LambdaQueryWrapper<ErpStockOut>()
            .like(StringUtils.hasText(outNum), ErpStockOut::getOutNum, outNum)
            .like(StringUtils.hasText(sourceNum), ErpStockOut::getSourceNum, sourceNum)
            .eq(status != null, ErpStockOut::getStatus, status)
            .orderByDesc(ErpStockOut::getId);
        IPage<ErpStockOut> page = mapper.selectPage(pageQuery.build(), qw);
        return PageResult.build(page);
    }

    public ErpStockOut getById(Long id) {
        return mapper.selectById(id);
    }

    public List<ErpStockOutItem> getItemsByEntryId(Long entryId) {
        return itemMapper.selectList(
            new LambdaQueryWrapper<ErpStockOutItem>().eq(ErpStockOutItem::getEntryId, entryId));
    }

    public OGoodsSkuStock getStock(Long skuId) {
        return stockMapper.selectOne(
            new LambdaQueryWrapper<OGoodsSkuStock>()
                .eq(OGoodsSkuStock::getSkuId, skuId));
    }

    @Transactional
    public void createDelivery(ErpStockOut stockOut, List<ErpStockOutItem> items) {
        stockOut.setOutNum("DEL" + System.currentTimeMillis());
        stockOut.setType(1);
        stockOut.setStatus(0);
        stockOut.setGoodsUnit(items.size());
        stockOut.setSpecUnit(items.size());
        stockOut.setSpecUnitTotal(items.stream().mapToInt(i -> i.getOriginalQuantity().intValue()).sum());
        stockOut.setCreateTime(new Date());
        mapper.insert(stockOut);

        for (ErpStockOutItem item : items) {
            item.setEntryId(stockOut.getId());
            item.setType(1);
            item.setStatus(0);
            item.setCreateTime(new Date());
            itemMapper.insert(item);
        }
    }

    @Transactional
    public void confirmOutbound(Long deliveryId) {
        ErpStockOut so = mapper.selectById(deliveryId);
        if (so == null) throw new RuntimeException("出库单不存在");
        if (so.getStatus() != 0) throw new RuntimeException("出库单已处理");

        List<ErpStockOutItem> items = itemMapper.selectList(
            new LambdaQueryWrapper<ErpStockOutItem>().eq(ErpStockOutItem::getEntryId, deliveryId));

        for (ErpStockOutItem item : items) {
            if (item.getSkuId() != null) {
                int qty = item.getOriginalQuantity().intValue();

                OGoodsSkuStock stock = stockMapper.selectOne(
                    new LambdaQueryWrapper<OGoodsSkuStock>()
                        .eq(OGoodsSkuStock::getSkuId, item.getSkuId()));
                if (stock != null) {
                    stock.setQuantity(Math.max((stock.getQuantity() != null ? stock.getQuantity() : 0) - qty, 0));
                    stock.setUpdateTime(new Date());
                    stockMapper.updateById(stock);
                }

                if (so.getWarehouseId() != null) {
                    OGoodsSkuStockWarehouse whStock = whStockMapper.selectOne(
                        new LambdaQueryWrapper<OGoodsSkuStockWarehouse>()
                            .eq(OGoodsSkuStockWarehouse::getWarehouseId, so.getWarehouseId())
                            .eq(OGoodsSkuStockWarehouse::getSkuId, item.getSkuId()));
                    if (whStock != null) {
                        whStock.setQuantity(Math.max((whStock.getQuantity() != null ? whStock.getQuantity() : 0) - qty, 0));
                        whStock.setUpdateTime(new Date());
                        whStockMapper.updateById(whStock);
                    }
                }

                item.setOutQuantity(item.getOriginalQuantity());
                item.setStatus(2);
                item.setCompleteTime(new Date());
                itemMapper.updateById(item);
            }
        }

        so.setOutTotal(so.getSpecUnitTotal());
        so.setStatus(2);
        so.setOutTime(new Date());
        so.setCompleteTime(new Date());
        mapper.updateById(so);

        OOrder order = orderMapper.selectById(so.getSourceId());
        if (order != null) {
            order.setOrderStatus(2);
            order.setUpdateTime(new Date());
            orderMapper.updateById(order);
        }
    }

    @Transactional
    public Long deliver(Long orderId, Long warehouseId, String warehouseName,
                        String logisticsCompany, String logisticsNo,
                        List<ErpStockOutItem> items, String remark) {
        OOrder order = orderMapper.selectById(orderId);
        if (order == null) throw new RuntimeException("订单不存在");
        if (order.getOrderStatus() != 1) throw new RuntimeException("订单不是待发货状态");

        order.setOrderStatus(2);
        order.setLogisticsCompany(logisticsCompany);
        order.setLogisticsNo(logisticsNo);
        order.setWarehouseId(warehouseId);
        order.setWarehouseName(warehouseName);
        order.setShipTime(new Date());
        order.setUpdateTime(new Date());
        orderMapper.updateById(order);

        ErpStockOut so = new ErpStockOut();
        so.setOutNum("DEL" + System.currentTimeMillis());
        so.setSourceNum(order.getOrderNum());
        so.setSourceId(orderId);
        so.setType(1);
        so.setWarehouseId(warehouseId);
        so.setWarehouseName(warehouseName);
        so.setRemark(remark);
        so.setShopId(0L);
        so.setMerchantId(0L);
        so.setStatus(2);
        so.setGoodsUnit(items.size());
        so.setSpecUnit(items.size());
        so.setSpecUnitTotal(items.stream().mapToInt(i -> i.getOriginalQuantity().intValue()).sum());
        so.setOutTotal(so.getSpecUnitTotal());
        so.setOutTime(new Date());
        so.setCompleteTime(new Date());
        so.setCreateTime(new Date());
        mapper.insert(so);

        for (ErpStockOutItem item : items) {
            item.setEntryId(so.getId());
            item.setType(1);
            item.setStatus(2);
            item.setOutQuantity(item.getOriginalQuantity());
            item.setCompleteTime(new Date());
            item.setCreateTime(new Date());
            itemMapper.insert(item);

            if (item.getSkuId() != null) {
                int qty = item.getOriginalQuantity().intValue();

                OGoodsSkuStock stock = stockMapper.selectOne(
                    new LambdaQueryWrapper<OGoodsSkuStock>().eq(OGoodsSkuStock::getSkuId, item.getSkuId()));
                if (stock != null) {
                    stock.setQuantity(Math.max((stock.getQuantity() != null ? stock.getQuantity() : 0) - qty, 0));
                    stock.setUpdateTime(new Date());
                    stockMapper.updateById(stock);
                }

                if (warehouseId != null) {
                    OGoodsSkuStockWarehouse whStock = whStockMapper.selectOne(
                        new LambdaQueryWrapper<OGoodsSkuStockWarehouse>()
                            .eq(OGoodsSkuStockWarehouse::getWarehouseId, warehouseId)
                            .eq(OGoodsSkuStockWarehouse::getSkuId, item.getSkuId()));
                    if (whStock != null) {
                        whStock.setQuantity(Math.max((whStock.getQuantity() != null ? whStock.getQuantity() : 0) - qty, 0));
                        whStock.setUpdateTime(new Date());
                        whStockMapper.updateById(whStock);
                    }
                }
            }
        }

        return so.getId();
    }
}