package cn.qihangerp.security;

import cn.qihangerp.mapper.SysRoleMapper;
import cn.qihangerp.model.SysMenu;
import cn.qihangerp.model.SysRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SysRoleManageService {

    /** Top-level menu ID of the system management module (only assignable to admin role) */
    private static final List<Long> SYSTEM_MENU_PARENT_IDS = Arrays.asList(13L);

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
        return getMenuTree(false);
    }

    public List<SysMenu> getMenuTree(boolean excludeSystem) {
        List<SysMenu> all = roleMapper.selectAllVisibleMenus();
        if (excludeSystem) {
            List<Long> systemIds = getSystemMenuIds();
            all = all.stream()
                    .filter(m -> !systemIds.contains(m.getMenuId()))
                    .collect(Collectors.toList());
        }
        return buildTree(all, 0L);
    }

    /**
     * Get all menu IDs belonging to the system management module,
     * recursively including all children of top-level system menus.
     */
    private List<Long> getSystemMenuIds() {
        List<SysMenu> allMenus = roleMapper.selectAllMenus();
        List<Long> systemIds = new ArrayList<>();
        for (Long parentId : SYSTEM_MENU_PARENT_IDS) {
            collectChildren(allMenus, parentId, systemIds);
        }
        return systemIds;
    }

    private void collectChildren(List<SysMenu> allMenus, Long parentId, List<Long> result) {
        result.add(parentId);
        for (SysMenu menu : allMenus) {
            if (Objects.equals(menu.getParentId(), parentId)) {
                collectChildren(allMenus, menu.getMenuId(), result);
            }
        }
    }

    public List<Long> getRoleMenuIds(Long roleId) {
        return roleMapper.selectMenuIdsByRoleId(roleId);
    }

    @Transactional
    public void updateRoleMenus(Long roleId, List<Long> menuIds) {
        // 目标角色不是 admin 时，自动过滤系统管理菜单
        SysRole role = roleMapper.selectById(roleId);
        if (role != null && !"admin".equals(role.getRoleKey())) {
            List<Long> systemMenuIds = getSystemMenuIds();
            menuIds = menuIds.stream()
                    .filter(id -> !systemMenuIds.contains(id))
                    .collect(Collectors.toList());
        }

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
