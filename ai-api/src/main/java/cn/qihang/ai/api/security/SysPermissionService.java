package cn.qihang.ai.api.security;

import cn.qihang.ai.api.mapper.SysUserMapper;
import cn.qihang.ai.business.model.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class SysPermissionService {

    @Autowired
    private SysUserMapper userMapper;

    public Set<String> getRolePermission(SysUser user) {
        return new HashSet<>(userMapper.selectRoleKeysByUserId(user.getUserId()));
    }

    public Set<String> getMenuPermission(SysUser user) {
        return new HashSet<>(userMapper.selectPermissionsByUserId(user.getUserId()));
    }
}
