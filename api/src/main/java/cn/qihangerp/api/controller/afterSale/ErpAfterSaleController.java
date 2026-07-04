package cn.qihangerp.api.controller.afterSale;

import cn.qihangerp.common.AjaxResult;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.model.ErpAfterSale;
import cn.qihangerp.model.ErpAfterSaleItem;
import cn.qihangerp.service.ErpAfterSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/erp-api/after-sale")
public class ErpAfterSaleController {
    @Autowired private ErpAfterSaleService service;

    @GetMapping("/list")
    public PageResult<ErpAfterSale> list(PageQuery pageQuery, String afterNum, String orderNum, Integer type, Integer status) {
        return service.list(afterNum, orderNum, type, status, pageQuery);
    }

    @GetMapping("/{id}")
    public AjaxResult getById(@PathVariable Long id) {
        ErpAfterSale as = service.getById(id);
        if (as == null) return AjaxResult.error(404, "售后单不存在");
        return AjaxResult.success(as);
    }

    @PostMapping("/create")
    public AjaxResult create(@RequestBody Map<String, Object> body) {
        ErpAfterSale afterSale = new ErpAfterSale();
        afterSale.setOrderId(body.get("orderId") != null ? ((Number) body.get("orderId")).longValue() : null);
        afterSale.setOrderNum((String) body.get("orderNum"));
        afterSale.setType(body.get("type") != null ? ((Number) body.get("type")).intValue() : 1);
        afterSale.setReason((String) body.get("reason"));
        afterSale.setAmount(body.get("amount") != null ? new BigDecimal(body.get("amount").toString()) : null);
        afterSale.setRemark((String) body.get("remark"));

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> itemMaps = (List<Map<String, Object>>) body.get("itemList");
        List<ErpAfterSaleItem> items = null;
        if (itemMaps != null) {
            items = itemMaps.stream().map(s -> {
                ErpAfterSaleItem item = new ErpAfterSaleItem();
                item.setOrderItemId(s.get("orderItemId") != null ? ((Number) s.get("orderItemId")).longValue() : null);
                item.setGoodsId(s.get("goodsId") != null ? ((Number) s.get("goodsId")).longValue() : null);
                item.setGoodsName((String) s.get("goodsName"));
                item.setGoodsImage((String) s.get("goodsImage"));
                item.setGoodsNum((String) s.get("goodsNum"));
                item.setSkuId(s.get("skuId") != null ? ((Number) s.get("skuId")).longValue() : null);
                item.setSkuCode((String) s.get("skuCode"));
                item.setSkuName((String) s.get("skuName"));
                item.setQuantity(s.get("quantity") != null ? ((Number) s.get("quantity")).intValue() : 1);
                item.setRefundAmount(s.get("refundAmount") != null ? new BigDecimal(s.get("refundAmount").toString()) : null);
                return item;
            }).toList();
        }

        service.create(afterSale, items);
        return AjaxResult.success();
    }

    @PostMapping("/{id}/approve")
    public AjaxResult approve(@PathVariable Long id) {
        try {
            service.approve(id);
            return AjaxResult.success();
        } catch (RuntimeException e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @PostMapping("/{id}/refund")
    public AjaxResult refund(@PathVariable Long id) {
        try {
            service.refund(id);
            return AjaxResult.success();
        } catch (RuntimeException e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @PostMapping("/{id}/returnStock")
    public AjaxResult returnStock(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Long warehouseId = body.get("warehouseId") != null ? ((Number) body.get("warehouseId")).longValue() : null;
        String warehouseName = (String) body.get("warehouseName");
        try {
            service.returnStock(id, warehouseId, warehouseName);
            return AjaxResult.success();
        } catch (RuntimeException e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}