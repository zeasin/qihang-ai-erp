package cn.qihangerp.api.controller.sys;
import cn.qihangerp.security.common.SecurityUtils;
import cn.qihangerp.common.AjaxResult;
import cn.qihangerp.model.SysOpenAuth;
import cn.qihangerp.service.SysOpenAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/sys-api/system/openAuth")
public class SysOpenAuthController {
    @Autowired private SysOpenAuthService openAuthService;
    private boolean notAdmin() {
        var u = SecurityUtils.getLoginUser();
        return u == null || u.getRoles().stream().noneMatch(r -> "admin".equals(r.getRoleKey()));
    }
    @GetMapping("/list") public AjaxResult list() {
        if (notAdmin()) return AjaxResult.error(403, "权限不足");
        return AjaxResult.success(openAuthService.list());
    }
    @PostMapping("/save") public AjaxResult save(@RequestBody SysOpenAuth auth) {
        if (notAdmin()) return AjaxResult.error(403, "权限不足");
        openAuthService.save(auth); return AjaxResult.success();
    }
    @DeleteMapping("/{id}") public AjaxResult delete(@PathVariable Long id) {
        if (notAdmin()) return AjaxResult.error(403, "权限不足");
        openAuthService.delete(id); return AjaxResult.success();
    }
}