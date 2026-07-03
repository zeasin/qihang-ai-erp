package cn.qihangerp.api.controller.sys;
import cn.qihangerp.security.common.SecurityUtils;
import cn.qihangerp.common.AjaxResult;
import cn.qihangerp.model.SysTask;
import cn.qihangerp.service.SysTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/sys-api/system/task")
public class SysTaskController {
    @Autowired private SysTaskService taskService;
    private boolean notAdmin() {
        var u = SecurityUtils.getLoginUser();
        return u == null || u.getRoles().stream().noneMatch(r -> "admin".equals(r.getRoleKey()));
    }
    @GetMapping("/list") public AjaxResult list() {
        if (notAdmin()) return AjaxResult.error(403, "权限不足");
        return AjaxResult.success(taskService.list());
    }
    @GetMapping("/{id}") public AjaxResult getById(@PathVariable Long id) {
        if (notAdmin()) return AjaxResult.error(403, "权限不足");
        return AjaxResult.success(taskService.getById(id));
    }
    @PutMapping public AjaxResult update(@RequestBody SysTask task) {
        if (notAdmin()) return AjaxResult.error(403, "权限不足");
        taskService.update(task); return AjaxResult.success();
    }
}