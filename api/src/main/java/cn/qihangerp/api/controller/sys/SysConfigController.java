package cn.qihangerp.api.controller.sys;
import cn.qihangerp.security.common.SecurityUtils;
import cn.qihangerp.common.AjaxResult;
import cn.qihangerp.model.SysConfig;
import cn.qihangerp.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/sys-api/system/config")
public class SysConfigController {
    @Autowired private SysConfigService configService;
    private boolean notAdmin() {
        var u = SecurityUtils.getLoginUser();
        return u == null || u.getRoles().stream().noneMatch(r -> "admin".equals(r.getRoleKey()));
    }
    @GetMapping("/list") public AjaxResult list() {
        if (notAdmin()) return AjaxResult.error(403, "权限不足");
        return AjaxResult.success(configService.list());
    }
    @PostMapping("/save") public AjaxResult save(@RequestBody SysConfig config) {
        if (notAdmin()) return AjaxResult.error(403, "权限不足");
        configService.save(config); return AjaxResult.success();
    }
    @DeleteMapping("/{id}") public AjaxResult delete(@PathVariable Long id) {
        if (notAdmin()) return AjaxResult.error(403, "权限不足");
        configService.delete(id); return AjaxResult.success();
    }
    @GetMapping("/get/{key}") public AjaxResult getValue(@PathVariable String key) {
        return AjaxResult.success(configService.getValue(key));
    }
}