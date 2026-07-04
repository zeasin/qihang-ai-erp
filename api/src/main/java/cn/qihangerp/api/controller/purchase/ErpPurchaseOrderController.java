package cn.qihangerp.api.controller.purchase;

import cn.qihangerp.api.controller.order.*;
import cn.qihangerp.common.AjaxResult;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.model.ErpPurchaseOrder;
import cn.qihangerp.model.ErpPurchaseOrderItem;
import cn.qihangerp.model.ErpStockIn;
import cn.qihangerp.service.ErpPurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/erp-api/purchase")
public class ErpPurchaseOrderController {
    @Autowired private ErpPurchaseOrderService service;

    @GetMapping("/list")
    public PageResult<ErpPurchaseOrder> list(PageQuery pageQuery, String orderNum, String supplierName, Integer status) {
        return service.list(orderNum, supplierName, status, pageQuery);
    }

    @GetMapping("/{id}")
    public AjaxResult getById(@PathVariable Long id) {
        ErpPurchaseOrder order = service.getById(id);
        if (order == null) return AjaxResult.error(404, "采购订单不存在");
        return AjaxResult.success(order);
    }

    @PostMapping("/save")
    public AjaxResult save(@RequestBody Map<String, Object> body) {
        ErpPurchaseOrder order = new ErpPurchaseOrder();
        order.setId(body.get("id") != null ? ((Number) body.get("id")).longValue() : null);
        order.setSupplierId(body.get("supplierId") != null ? ((Number) body.get("supplierId")).longValue() : null);
        order.setSupplierName((String) body.get("supplierName"));
        order.setRemark((String) body.get("remark"));
        if (body.get("orderTime") != null && !body.get("orderTime").toString().isEmpty()) {
            try {
                order.setOrderTime(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(body.get("orderTime").toString()));
            } catch (Exception ignored) {}
        }

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> itemMaps = (List<Map<String, Object>>) body.get("itemList");
        List<ErpPurchaseOrderItem> items = null;
        if (itemMaps != null) {
            items = itemMaps.stream().map(s -> {
                ErpPurchaseOrderItem item = new ErpPurchaseOrderItem();
                item.setGoodsId(s.get("goodsId") != null ? ((Number) s.get("goodsId")).longValue() : null);
                item.setSkuId(s.get("skuId") != null ? ((Number) s.get("skuId")).longValue() : null);
                item.setGoodsName((String) s.get("goodsName"));
                item.setGoodsImage((String) s.get("goodsImage"));
                item.setGoodsNum((String) s.get("goodsNum"));
                item.setSkuCode((String) s.get("skuCode"));
                item.setSkuName((String) s.get("skuName"));
                item.setQuantity(s.get("quantity") != null ? ((Number) s.get("quantity")).intValue() : 1);
                item.setPurchasePrice(s.get("purchasePrice") != null ? new java.math.BigDecimal(s.get("purchasePrice").toString()) : null);
                item.setRemark((String) s.get("remark"));
                return item;
            }).toList();
        }

        service.save(order, items);
        return AjaxResult.success();
    }

    @DeleteMapping("/{id}")
    public AjaxResult delete(@PathVariable Long id) {
        service.delete(id);
        return AjaxResult.success();
    }

    @PostMapping("/{id}/stockIn")
    public AjaxResult stockIn(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Long warehouseId = body.get("warehouseId") != null ? ((Number) body.get("warehouseId")).longValue() : null;
        String warehouseName = (String) body.get("warehouseName");
        try {
            service.stockIn(id, warehouseId, warehouseName);
            return AjaxResult.success();
        } catch (RuntimeException e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}