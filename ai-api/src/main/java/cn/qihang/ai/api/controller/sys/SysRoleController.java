package cn.qihang.ai.api.controller.sys;

import cn.qihang.ai.api.security.SysRoleManageService;
import cn.qihang.ai.business.model.SysMenu;
import cn.qihang.ai.business.model.SysRole;
import cn.qihang.ai.common.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sys-api/system")
public class SysRoleController {

    @Autowired
    private SysRoleManageService roleManageService;

    @GetMapping("/role/list")
    public AjaxResult listRoles() {
        return AjaxResult.success(roleManageService.listRoles());
    }

    @PostMapping("/role/save")
    public AjaxResult saveRole(@RequestBody SysRole role) {
        roleManageService.saveRole(role);
        return AjaxResult.success();
    }

    @DeleteMapping("/role/{id}")
    public AjaxResult deleteRole(@PathVariable Long id) {
        roleManageService.deleteRole(id);
        return AjaxResult.success();
    }

    @GetMapping("/menu/tree")
    public AjaxResult menuTree() {
        return AjaxResult.success(roleManageService.getMenuTree());
    }

    @GetMapping("/role/{id}/menus")
    public AjaxResult getRoleMenus(@PathVariable Long id) {
        return AjaxResult.success(roleManageService.getRoleMenuIds(id));
    }

    @PutMapping("/role/{id}/menus")
    public AjaxResult updateRoleMenus(@PathVariable Long id, @RequestBody List<Long> menuIds) {
        roleManageService.updateRoleMenus(id, menuIds);
        return AjaxResult.success();
    }

    @GetMapping("/user/list")
    public AjaxResult listUsers() {
        return AjaxResult.success(roleManageService.listUsers());
    }

    @PutMapping("/user/{id}/roles")
    public AjaxResult updateUserRoles(@PathVariable Long id, @RequestBody List<Long> roleIds) {
        roleManageService.updateUserRoles(id, roleIds);
        return AjaxResult.success();
    }

    @PostMapping("/menu/save")
    public AjaxResult saveMenu(@RequestBody cn.qihang.ai.business.model.SysMenu menu) {
        roleManageService.saveMenu(menu);
        return AjaxResult.success();
    }

    @DeleteMapping("/menu/{id}")
    public AjaxResult deleteMenu(@PathVariable Long id) {
        roleManageService.deleteMenu(id);
        return AjaxResult.success();
    }
}
