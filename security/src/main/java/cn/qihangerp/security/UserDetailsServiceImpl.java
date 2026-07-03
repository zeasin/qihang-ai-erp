package cn.qihangerp.security;

import cn.qihangerp.mapper.SysUserMapper;
import cn.qihangerp.model.SysRole;
import cn.qihangerp.model.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = userMapper.selectByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }
        List<SysRole> roles = userMapper.selectRolesByUserId(user.getUserId());
        Set<String> permissions = userMapper.selectPermissionsByUserId(user.getUserId());
        return new LoginUser(user, roles, permissions);
    }
}
