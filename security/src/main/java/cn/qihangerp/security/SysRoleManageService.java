package cn.qihangerp.security;

import cn.qihangerp.mapper.SysRoleMapper;
import cn.qihangerp.model.SysMenu;
import cn.qihangerp.model.SysRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.*;
import java.util.stream.Collectors;

@Service
public class SysRoleManageService {

    @Autowired
    private cn.qihangerp.mapper.SysMenuMapper menuMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    public List<SysRole> listRoles() {
        return roleMapper.selectList(null);
    }

    public void saveRole(SysRole role) {
        if (role.getRoleId() == null) {
            roleMapper.insert(role);
        } else {
            roleMapper.updateById(role);
        }
    }

    public void deleteRole(Long roleId) {
        roleMapper.deleteById(roleId);
        roleMapper.deleteRoleMenus(roleId);
    }

    public List<SysMenu> getMenuTree() {
        List<SysMenu> all = roleMapper.selectAllVisibleMenus();
        return buildTree(all, 0L);
    }

    public List<Long> getRoleMenuIds(Long roleId) {
        return roleMapper.selectMenuIdsByRoleId(roleId);
    }

    @Transactional
    public void updateRoleMenus(Long roleId, List<Long> menuIds) {
        roleMapper.deleteRoleMenus(roleId);
        for (Long menuId : menuIds) {
            roleMapper.insertRoleMenu(roleId, menuId);
        }
    }

    private List<SysMenu> buildTree(List<SysMenu> all, Long parentId) {
        return all.stream()
                .filter(m -> Objects.equals(m.getParentId(), parentId))
                .peek(m -> m.setChildren(buildTree(all, m.getMenuId())))
                .collect(Collectors.toList());
    }

    public void saveMenu(SysMenu menu) {
        if (menu.getMenuId() == null) {
            menu.setCreateTime(new Date());
            menuMapper.insert(menu);
        } else {
            menuMapper.updateById(menu);
        }
    }

    public void deleteMenu(Long menuId) {
        menuMapper.deleteById(menuId);
        roleMapper.deleteRoleMenuByMenuId(menuId);
    }


    public List<Map<String, Object>> listUsers() {
        List<Map<String, Object>> users = roleMapper.selectAllUsers();
        for (Map<String, Object> u : users) {
            Long uid = ((Number) u.get("user_id")).longValue();
            List<Long> roleIds = roleMapper.selectRoleIdsByUserId(uid);
            u.put("roleIds", roleIds);
        }
        return users;
    }

    @Transactional
    public void updateUserRoles(Long userId, List<Long> roleIds) {
        roleMapper.deleteUserRoles(userId);
        for (Long roleId : roleIds) {
            roleMapper.insertUserRole(userId, roleId);
        }
    }
}
