package cn.qihangerp.api.controller.basic;

import cn.qihangerp.common.AjaxResult;
import cn.qihangerp.model.GoodsCategory;
import cn.qihangerp.security.common.SecurityUtils;
import cn.qihangerp.service.GoodsCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sys-api/basic/category")
public class GoodsCategoryController {
    @Autowired private GoodsCategoryService service;

    private boolean notAdmin() {
        var u = SecurityUtils.getLoginUser();
        return u == null || u.getRoles().stream().noneMatch(r -> "admin".equals(r.getRoleKey()));
    }

    @GetMapping("/list")
    public AjaxResult list() {
        return AjaxResult.success(service.list());
    }

    @PostMapping("/save")
    public AjaxResult save(@RequestBody GoodsCategory category) {
        if (notAdmin()) return AjaxResult.error(403, "权限不足");
        service.save(category);
        return AjaxResult.success();
    }

    @DeleteMapping("/{id}")
    public AjaxResult delete(@PathVariable Long id) {
        if (notAdmin()) return AjaxResult.error(403, "权限不足");
        service.delete(id);
        return AjaxResult.success();
    }
}