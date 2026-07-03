package cn.qihangerp.api.controller.sys;

import cn.qihangerp.common.AjaxResult;
import cn.qihangerp.model.ShopPlatform;
import cn.qihangerp.security.common.SecurityUtils;
import cn.qihangerp.service.ShopPlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/sys-api/system/platform")
public class SysPlatformController {
    @Autowired private ShopPlatformService service;
    private boolean notAdmin() {
        var u = SecurityUtils.getLoginUser();
        return u == null || u.getRoles().stream().noneMatch(r -> "admin".equals(r.getRoleKey()));
    }
    @GetMapping("/list") public AjaxResult list() { return AjaxResult.success(service.list()); }
    @PostMapping("/save") public AjaxResult save(@RequestBody ShopPlatform p) {
        if (notAdmin()) return AjaxResult.error(403, "权限不足");
        service.save(p); return AjaxResult.success();
    }
    @DeleteMapping("/{id}") public AjaxResult delete(@PathVariable Integer id) {
        if (notAdmin()) return AjaxResult.error(403, "权限不足");
        service.delete(id); return AjaxResult.success();
    }
}