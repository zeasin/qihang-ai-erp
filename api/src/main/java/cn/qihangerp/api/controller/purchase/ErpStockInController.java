package cn.qihangerp.api.controller.purchase;

import cn.qihangerp.common.AjaxResult;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.model.ErpStockIn;
import cn.qihangerp.service.ErpPurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/erp-api/purchase/stock-in")
public class ErpStockInController {
    @Autowired private ErpPurchaseOrderService service;

    @GetMapping("/list")
    public PageResult<ErpStockIn> list(PageQuery pageQuery, String inNum, String sourceNo) {
        return service.stockInList(inNum, sourceNo, pageQuery);
    }

    @GetMapping("/{id}")
    public AjaxResult getById(@PathVariable Long id) {
        ErpStockIn si = service.getStockInById(id);
        if (si == null) return AjaxResult.error(404, "入库单不存在");
        return AjaxResult.success(si);
    }
}