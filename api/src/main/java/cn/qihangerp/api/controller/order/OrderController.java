package cn.qihangerp.api.controller.order;

import cn.qihangerp.common.AjaxResult;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.model.OOrder;
import cn.qihangerp.model.OOrderItem;
import cn.qihangerp.service.OOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/erp-api/order")
public class OrderController {
    @Autowired private OOrderService service;

    @GetMapping("/list")
    public PageResult<OOrder> list(PageQuery pageQuery, String orderNum, String receiverName,
                                    Integer orderStatus, String startTime, String endTime) {
        return service.list(orderNum, receiverName, orderStatus, startTime, endTime, pageQuery);
    }

    @GetMapping("/{id}")
    public AjaxResult getById(@PathVariable Long id) {
        OOrder order = service.getById(id);
        if (order == null) return AjaxResult.error(404, "订单不存在");
        return AjaxResult.success(order);
    }

    @PostMapping("/{id}/pay")
    public AjaxResult pay(@PathVariable Long id) {
        try {
            service.pay(id);
            return AjaxResult.success();
        } catch (RuntimeException e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @PostMapping("/save")
    public AjaxResult save(@RequestBody Map<String, Object> body) {
        OOrder order = new OOrder();
        order.setOrderNum((String) body.get("orderNum"));
        order.setOrderMode(1);
        order.setOrderStatus(body.get("orderStatus") != null ? ((Number) body.get("orderStatus")).intValue() : 0);
        order.setGoodsAmount(body.get("goodsAmount") != null ? new java.math.BigDecimal(body.get("goodsAmount").toString()) : null);
        order.setAmount(body.get("amount") != null ? new java.math.BigDecimal(body.get("amount").toString()) : null);
        order.setPayment(body.get("payment") != null ? new java.math.BigDecimal(body.get("payment").toString()) : null);
        order.setPostFee(body.get("postFee") != null ? new java.math.BigDecimal(body.get("postFee").toString()) : null);
        order.setDiscountAmount(body.get("discountAmount") != null ? new java.math.BigDecimal(body.get("discountAmount").toString()) : null);
        order.setReceiverName((String) body.get("receiverName"));
        order.setReceiverMobile((String) body.get("receiverMobile"));
        order.setProvince((String) body.get("province"));
        order.setCity((String) body.get("city"));
        order.setTown((String) body.get("town"));
        order.setAddress((String) body.get("address"));
        order.setRemark((String) body.get("remark"));

        Object orderTimeObj = body.get("orderTime");
        if (orderTimeObj != null && !orderTimeObj.toString().isEmpty()) {
            try {
                order.setOrderTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(orderTimeObj.toString()));
            } catch (Exception ignored) {}
        }

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> itemMaps = (List<Map<String, Object>>) body.get("itemList");
        List<OOrderItem> items = null;
        if (itemMaps != null) {
            items = itemMaps.stream().map(s -> {
                OOrderItem item = new OOrderItem();
                item.setGoodsId(s.get("goodsId") != null ? ((Number) s.get("goodsId")).longValue() : null);
                item.setGoodsSkuId(s.get("goodsSkuId") != null ? ((Number) s.get("goodsSkuId")).longValue() : null);
                item.setGoodsTitle((String) s.get("goodsTitle"));
                item.setGoodsImg((String) s.get("goodsImg"));
                item.setGoodsNum((String) s.get("goodsNum"));
                item.setGoodsSpec((String) s.get("goodsSpec"));
                item.setSkuNum((String) s.get("skuNum"));
                item.setGoodsPrice(s.get("goodsPrice") != null ? new java.math.BigDecimal(s.get("goodsPrice").toString()) : null);
                item.setQuantity(s.get("quantity") != null ? ((Number) s.get("quantity")).intValue() : 1);
                item.setItemAmount(s.get("itemAmount") != null ? new java.math.BigDecimal(s.get("itemAmount").toString()) : null);
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
}