package cn.qihangerp.security;

import cn.qihangerp.model.SysMenu;
import cn.qihangerp.model.SysRole;
import cn.qihangerp.model.SysUser;
import cn.qihangerp.common.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import cn.qihangerp.security.common.SecurityUtils;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SysLoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private cn.qihangerp.mapper.SysUserMapper userMapper;

    public String login(String username, String password, String code, String uuid) {
        // 验证码校验（暂略，可后续集成）
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        } catch (org.springframework.security.authentication.BadCredentialsException e) {
            throw new RuntimeException("密码不正确");
        } catch (org.springframework.security.core.userdetails.UsernameNotFoundException e) {
            throw new RuntimeException("账号不存在");
        }
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        // 记录登录信息
        userMapper.updateLoginInfo(loginUser.getUserId(),
            SecurityUtils.getRequest() != null ? SecurityUtils.getRequest().getRemoteAddr() : "127.0.0.1");
        return tokenService.createToken(loginUser);
    }

    public List<SysMenu> selectMenuTreeByUserId(Long userId) {
        return userMapper.selectMenusByUserId(userId);
    }

    public List<AjaxResult.RouterVo> buildMenus(List<SysMenu> menus) {
        // 构建前端路由树（简化版）
        return menus.stream().filter(m -> m.getMenuType().equals("C")).map(m -> {
            AjaxResult.RouterVo vo = new AjaxResult.RouterVo();
            vo.setPath(m.getPath());
            vo.setName(m.getMenuName());
            vo.setComponent(m.getComponent());
            vo.setMeta(new AjaxResult.MetaVo(m.getMenuName(), m.getIcon()));
            return vo;
        }).collect(Collectors.toList());
    }
}
