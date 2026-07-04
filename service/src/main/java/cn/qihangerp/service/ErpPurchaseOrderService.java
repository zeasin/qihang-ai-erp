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

@Service
public class ErpPurchaseOrderService {
    @Autowired private ErpPurchaseOrderMapper mapper;
    @Autowired private ErpPurchaseOrderItemMapper itemMapper;
    @Autowired private ErpStockInMapper stockInMapper;
    @Autowired private ErpStockInItemMapper stockInItemMapper;
    @Autowired private OGoodsSkuStockMapper stockMapper;
    @Autowired private OGoodsSkuStockWarehouseMapper whStockMapper;

    public PageResult<ErpPurchaseOrder> list(String orderNum, String supplierName, Integer status, PageQuery pageQuery) {
        var qw = new LambdaQueryWrapper<ErpPurchaseOrder>()
            .like(StringUtils.hasText(orderNum), ErpPurchaseOrder::getOrderNum, orderNum)
            .like(StringUtils.hasText(supplierName), ErpPurchaseOrder::getSupplierName, supplierName)
            .eq(status != null, ErpPurchaseOrder::getStatus, status)
            .orderByDesc(ErpPurchaseOrder::getId);
        IPage<ErpPurchaseOrder> page = mapper.selectPage(pageQuery.build(), qw);
        List<ErpPurchaseOrder> records = page.getRecords();
        if (!records.isEmpty()) {
            List<Long> ids = records.stream().map(ErpPurchaseOrder::getId).toList();
            List<ErpPurchaseOrderItem> allItems = itemMapper.selectList(
                new LambdaQueryWrapper<ErpPurchaseOrderItem>().in(ErpPurchaseOrderItem::getPurchaseOrderId, ids));
            var grouped = allItems.stream().collect(java.util.stream.Collectors.groupingBy(ErpPurchaseOrderItem::getPurchaseOrderId));
            records.forEach(o -> o.setItemList(grouped.get(o.getId())));
        }
        return PageResult.build(page);
    }

    public ErpPurchaseOrder getById(Long id) {
        ErpPurchaseOrder order = mapper.selectById(id);
        if (order != null) {
            List<ErpPurchaseOrderItem> items = itemMapper.selectList(
                new LambdaQueryWrapper<ErpPurchaseOrderItem>().eq(ErpPurchaseOrderItem::getPurchaseOrderId, id));
            order.setItemList(items);
        }
        return order;
    }

    @Transactional
    public void save(ErpPurchaseOrder order, List<ErpPurchaseOrderItem> items) {
        if (order.getId() == null) {
            order.setOrderNum("PO" + System.currentTimeMillis());
            order.setStatus(0);
            order.setCreateTime(new Date());
            mapper.insert(order);
        } else {
            mapper.updateById(order);
            itemMapper.delete(new LambdaQueryWrapper<ErpPurchaseOrderItem>()
                .eq(ErpPurchaseOrderItem::getPurchaseOrderId, order.getId()));
        }
        int goodsUnit = 0;
        int specUnit = 0;
        int specUnitTotal = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;
        if (items != null) {
            var distinctGoods = items.stream().map(ErpPurchaseOrderItem::getGoodsId).distinct().count();
            goodsUnit = (int) distinctGoods;
            specUnit = items.size();
            for (ErpPurchaseOrderItem item : items) {
                item.setPurchaseOrderId(order.getId());
                item.setPurchaseOrderNum(order.getOrderNum());
                item.setCreateTime(new Date());
                if (item.getQuantity() == null) item.setQuantity(0);
                if (item.getInQuantity() == null) item.setInQuantity(0);
                if (item.getPurchasePrice() == null) item.setPurchasePrice(BigDecimal.ZERO);
                item.setTotalAmount(item.getPurchasePrice().multiply(BigDecimal.valueOf(item.getQuantity())));
                specUnitTotal += item.getQuantity();
                totalAmount = totalAmount.add(item.getTotalAmount());
                itemMapper.insert(item);
            }
        }
        order.setGoodsUnit(goodsUnit);
        order.setSpecUnit(specUnit);
        order.setSpecUnitTotal(specUnitTotal);
        order.setTotalAmount(totalAmount);
        mapper.updateById(order);
    }

    public void delete(Long id) {
        itemMapper.delete(new LambdaQueryWrapper<ErpPurchaseOrderItem>().eq(ErpPurchaseOrderItem::getPurchaseOrderId, id));
        mapper.deleteById(id);
    }

    @Transactional
    public void stockIn(Long purchaseOrderId, Long warehouseId, String warehouseName) {
        ErpPurchaseOrder po = mapper.selectById(purchaseOrderId);
        if (po == null) throw new RuntimeException("采购订单不存在");
        if (po.getStatus() == 2) throw new RuntimeException("采购订单已完成入库");
        if (po.getStatus() == 11) throw new RuntimeException("采购订单已取消");

        List<ErpPurchaseOrderItem> items = itemMapper.selectList(
            new LambdaQueryWrapper<ErpPurchaseOrderItem>()
                .eq(ErpPurchaseOrderItem::getPurchaseOrderId, purchaseOrderId));

        int inTotal = 0;
        boolean allComplete = true;
        for (ErpPurchaseOrderItem item : items) {
            int remain = (item.getQuantity() != null ? item.getQuantity() : 0)
                - (item.getInQuantity() != null ? item.getInQuantity() : 0);
            if (remain > 0) {
                item.setInQuantity((item.getInQuantity() != null ? item.getInQuantity() : 0) + remain);
                item.setUpdateTime(new Date());
                itemMapper.updateById(item);
                inTotal += remain;

                if (item.getSkuId() != null) {
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
                        stock.setQuantity(remain);
                        stock.setCreateTime(new Date());
                        stockMapper.insert(stock);
                    } else {
                        stock.setQuantity((stock.getQuantity() != null ? stock.getQuantity() : 0) + remain);
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
                            whStock.setQuantity(remain);
                            whStock.setCreateTime(new Date());
                            whStockMapper.insert(whStock);
                        } else {
                            whStock.setQuantity((whStock.getQuantity() != null ? whStock.getQuantity() : 0) + remain);
                            whStock.setUpdateTime(new Date());
                            whStockMapper.updateById(whStock);
                        }
                    }
                }
            }
        }

        ErpStockIn si = new ErpStockIn();
        si.setInNum("SI" + System.currentTimeMillis());
        si.setType(2);
        si.setSourceNo(po.getOrderNum());
        si.setSourceId(purchaseOrderId);
        si.setGoodsUnit(po.getGoodsUnit());
        si.setSpecUnit(items.size());
        si.setSpecUnitTotal(inTotal);
        si.setInTotal(inTotal);
        si.setStatus(2);
        si.setInTime(new Date());
        si.setCompleteTime(new Date());
        si.setWarehouseId(warehouseId);
        si.setWarehouseName(warehouseName);
        si.setCreateTime(new Date());
        stockInMapper.insert(si);

        for (ErpPurchaseOrderItem item : items) {
            int inQty = (item.getInQuantity() != null ? item.getInQuantity() : 0)
                - ((item.getInQuantity() != null && item.getQuantity() != null
                    && item.getInQuantity() >= item.getQuantity())
                    ? 0 : 0);
            // Calculate the actual qty being stocked in this operation
            int thisIn = (item.getQuantity() != null ? item.getQuantity() : 0)
                - ((item.getInQuantity() != null ? item.getInQuantity() : 0) - (item.getQuantity() != null ? item.getQuantity() : 0) + (item.getInQuantity() != null ? item.getInQuantity() : 0));
            // simplified: thisIn = quantity - previousInQuantity
            int prevIn = (item.getInQuantity() != null ? item.getInQuantity() : 0) - (item.getQuantity() != null ? item.getQuantity() : 0) + (item.getQuantity() != null ? item.getQuantity() : 0);
            // Actually the inQty is just the full remaining quantity that was added above.
            // Let's just use the quantity as is since we already updated inQuantity above
            ErpStockInItem siItem = new ErpStockInItem();
            siItem.setStockInId(si.getId());
            siItem.setStockInType(2);
            siItem.setSourceNo(po.getOrderNum());
            siItem.setSourceId(purchaseOrderId);
            siItem.setSourceItemId(item.getId());
            siItem.setGoodsId(item.getGoodsId());
            siItem.setGoodsNum(item.getGoodsNum());
            siItem.setGoodsName(item.getGoodsName());
            siItem.setGoodsImage(item.getGoodsImage());
            siItem.setSkuId(item.getSkuId());
            siItem.setSkuCode(item.getSkuCode());
            siItem.setSkuName(item.getSkuName());
            siItem.setQuantity(item.getQuantity());
            siItem.setInQuantity(item.getInQuantity());
            siItem.setWarehouseId(warehouseId);
            siItem.setStatus(2);
            siItem.setCreateTime(new Date());
            stockInItemMapper.insert(siItem);
        }

        po.setSpecUnitTotal(inTotal);
        po.setCompleteTime(new Date());
        po.setStatus(2);
        mapper.updateById(po);
    }

    public PageResult<ErpStockIn> stockInList(String inNum, String sourceNo, PageQuery pageQuery) {
        var qw = new LambdaQueryWrapper<ErpStockIn>()
            .like(StringUtils.hasText(inNum), ErpStockIn::getInNum, inNum)
            .like(StringUtils.hasText(sourceNo), ErpStockIn::getSourceNo, sourceNo)
            .eq(ErpStockIn::getType, 2)
            .orderByDesc(ErpStockIn::getId);
        IPage<ErpStockIn> page = stockInMapper.selectPage(pageQuery.build(), qw);
        return PageResult.build(page);
    }

    public ErpStockIn getStockInById(Long id) {
        ErpStockIn si = stockInMapper.selectById(id);
        if (si != null) {
            List<ErpStockInItem> items = stockInItemMapper.selectList(
                new LambdaQueryWrapper<ErpStockInItem>().eq(ErpStockInItem::getStockInId, id));
            si.setItemList(items);
        }
        return si;
    }
}