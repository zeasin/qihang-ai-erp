package cn.qihangerp.api.controller.basic;

import cn.qihangerp.common.AjaxResult;
import cn.qihangerp.model.ErpSupplier;
import cn.qihangerp.security.common.SecurityUtils;
import cn.qihangerp.service.ErpSupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sys-api/basic/supplier")
public class ErpSupplierController {
    @Autowired private ErpSupplierService service;

    private boolean notAdmin() {
        var u = SecurityUtils.getLoginUser();
        return u == null || u.getRoles().stream().noneMatch(r -> "admin".equals(r.getRoleKey()));
    }

    @GetMapping("/list")
    public AjaxResult list(ErpSupplier bo) {
        return AjaxResult.success(service.list(bo));
    }

    @PostMapping("/save")
    public AjaxResult save(@RequestBody ErpSupplier supplier) {
        if (notAdmin()) return AjaxResult.error(403, "权限不足");
        service.save(supplier);
        return AjaxResult.success();
    }

    @DeleteMapping("/{id}")
    public AjaxResult delete(@PathVariable Long id) {
        if (notAdmin()) return AjaxResult.error(403, "权限不足");
        service.delete(id);
        return AjaxResult.success();
    }
}