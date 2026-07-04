package cn.qihangerp.api.controller.delivery;

import cn.qihangerp.common.AjaxResult;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.model.ErpStockOut;
import cn.qihangerp.model.ErpStockOutItem;
import cn.qihangerp.model.OGoodsSkuStock;
import cn.qihangerp.service.ErpStockOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/erp-api/delivery")
public class ErpStockOutController {
    @Autowired private ErpStockOutService service;

    @GetMapping("/list")
    public PageResult<ErpStockOut> list(PageQuery pageQuery, String outNum, String sourceNum, Integer status) {
        return service.list(outNum, sourceNum, status, pageQuery);
    }

    @GetMapping("/{id}")
    public AjaxResult getById(@PathVariable Long id) {
        ErpStockOut d = service.getById(id);
        if (d == null) return AjaxResult.error(404, "发货单不存在");
        var items = service.getItemsByEntryId(id);
        return AjaxResult.success(new Object(){ ErpStockOut delivery = d; List<ErpStockOutItem> itemList = items; });
    }

    @GetMapping("/stockCheck")
    public AjaxResult stockCheck(Long skuId) {
        if (skuId == null) return AjaxResult.error("参数不全");
        OGoodsSkuStock stock = service.getStock(skuId);
        return AjaxResult.success(stock != null ? stock : new Object(){ int quantity = 0; });
    }

    @PostMapping("/deliver")
    public AjaxResult deliver(@RequestBody Map<String, Object> body) {
        Object whId = body.get("warehouseId");
        if (whId == null) return AjaxResult.error("请选择仓库");
        Object orderId = body.get("orderId");
        if (orderId == null) return AjaxResult.error("订单ID不能为空");

        String logisticsCompany = (String) body.get("logisticsCompany");
        String logisticsNo = (String) body.get("logisticsNo");
        String remark = (String) body.get("remark");

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> itemMaps = (List<Map<String, Object>>) body.get("itemList");
        List<ErpStockOutItem> items = itemMaps != null ? itemMaps.stream().map(s -> {
            ErpStockOutItem item = new ErpStockOutItem();
            item.setSourceOrderItemId(s.get("orderItemId") != null ? ((Number) s.get("orderItemId")).longValue() : null);
            item.setGoodsId(s.get("goodsId") != null ? ((Number) s.get("goodsId")).longValue() : null);
            item.setSkuId(s.get("goodsSkuId") != null ? ((Number) s.get("goodsSkuId")).longValue() : null);
            item.setGoodsName((String) s.get("goodsTitle"));
            item.setGoodsImage((String) s.get("goodsImg"));
            item.setSkuName((String) s.get("goodsSpec"));
            item.setSkuCode((String) s.get("skuNum"));
            item.setOriginalQuantity(s.get("quantity") != null ? ((Number) s.get("quantity")).longValue() : 1L);
            item.setWarehouseId(((Number) whId).longValue());
            item.setShopId(0L);
            item.setMerchantId(0L);
            item.setPurPrice(java.math.BigDecimal.ZERO);
            return item;
        }).toList() : List.of();

        try {
            Long deliveryId = service.deliver(
                ((Number) orderId).longValue(),
                ((Number) whId).longValue(),
                (String) body.get("warehouseName"),
                logisticsCompany, logisticsNo, items, remark);
            return AjaxResult.success(deliveryId);
        } catch (RuntimeException e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @PostMapping("/{id}/confirm")
    public AjaxResult confirm(@PathVariable Long id) {
        try {
            service.confirmOutbound(id);
            return AjaxResult.success();
        } catch (RuntimeException e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}