package cn.qihang.ai.api.controller.sys;

import cn.qihang.ai.api.security.LoginUser;
import cn.qihang.ai.api.security.SysRoleManageService;
import cn.qihang.ai.api.security.common.SecurityUtils;
import cn.qihang.ai.business.model.SysMenu;
import cn.qihang.ai.business.model.SysRole;
import cn.qihang.ai.common.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sys-api/system")
public class SysRoleController {

    @Autowired
    private SysRoleManageService roleManageService;

    private boolean isNotAdmin() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null) return true;
        return loginUser.getRoles().stream().noneMatch(r -> "admin".equals(r.getRoleKey()));
    }

    @GetMapping("/role/list")
    public AjaxResult listRoles() { if (isNotAdmin()) return AjaxResult.error(403, "权限不足，仅管理员可操作"); return AjaxResult.success(roleManageService.listRoles()); }

    @PostMapping("/role/save")
    public AjaxResult saveRole(@RequestBody SysRole role) { if (isNotAdmin()) return AjaxResult.error(403, "权限不足，仅管理员可操作"); roleManageService.saveRole(role); return AjaxResult.success(); }

    @DeleteMapping("/role/{id}")
    public AjaxResult deleteRole(@PathVariable Long id) { if (isNotAdmin()) return AjaxResult.error(403, "权限不足，仅管理员可操作"); roleManageService.deleteRole(id); return AjaxResult.success(); }

    @GetMapping("/menu/tree")
    public AjaxResult menuTree() { return AjaxResult.success(roleManageService.getMenuTree()); }

    @GetMapping("/role/{id}/menus")
    public AjaxResult getRoleMenus(@PathVariable Long id) { if (isNotAdmin()) return AjaxResult.error(403, "权限不足，仅管理员可操作"); return AjaxResult.success(roleManageService.getRoleMenuIds(id)); }

    @PutMapping("/role/{id}/menus")
    public AjaxResult updateRoleMenus(@PathVariable Long id, @RequestBody List<Long> menuIds) { if (isNotAdmin()) return AjaxResult.error(403, "权限不足，仅管理员可操作"); roleManageService.updateRoleMenus(id, menuIds); return AjaxResult.success(); }

    @GetMapping("/user/list")
    public AjaxResult listUsers() { if (isNotAdmin()) return AjaxResult.error(403, "权限不足，仅管理员可操作"); return AjaxResult.success(roleManageService.listUsers()); }

    @PutMapping("/user/{id}/roles")
    public AjaxResult updateUserRoles(@PathVariable Long id, @RequestBody List<Long> roleIds) { if (isNotAdmin()) return AjaxResult.error(403, "权限不足，仅管理员可操作"); roleManageService.updateUserRoles(id, roleIds); return AjaxResult.success(); }

    @PostMapping("/menu/save")
    public AjaxResult saveMenu(@RequestBody SysMenu menu) { if (isNotAdmin()) return AjaxResult.error(403, "权限不足，仅管理员可操作"); roleManageService.saveMenu(menu); return AjaxResult.success(); }

    @DeleteMapping("/menu/{id}")
    public AjaxResult deleteMenu(@PathVariable Long id) { if (isNotAdmin()) return AjaxResult.error(403, "权限不足，仅管理员可操作"); roleManageService.deleteMenu(id); return AjaxResult.success(); }
}
