package cn.qihang.ai.api.controller.sys;

import cn.qihang.ai.api.security.LoginUser;
import cn.qihang.ai.api.security.SysLoginService;
import cn.qihang.ai.api.security.TokenService;
import cn.qihang.ai.api.security.common.Constants;
import cn.qihang.ai.api.security.common.SecurityUtils;
import cn.qihang.ai.business.model.LoginBody;
import cn.qihang.ai.business.model.SysMenu;
import cn.qihang.ai.common.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SysLoginController {

    @Autowired
    private SysLoginService loginService;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/api/sys-api/login")
    public AjaxResult login(@RequestBody LoginBody loginBody) {
        try {
            AjaxResult ajax = AjaxResult.success();
            String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(),
                loginBody.getCode(), loginBody.getUuid());
            ajax.put(Constants.TOKEN, token);
            return ajax;
        } catch (RuntimeException e) {
            return AjaxResult.error(401, e.getMessage());
        }
    }

    @GetMapping("/api/sys-api/getInfo")
    public AjaxResult getInfo() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null) return AjaxResult.error(401, "未登录");
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", loginUser.getUser());
        ajax.put("roles", loginUser.getRoles());
        ajax.put("permissions", loginUser.getPermissions());
        return ajax;
    }

    @GetMapping("/api/sys-api/getRouters")
    public AjaxResult getRouters() {
        Long userId = SecurityUtils.getUserId();
        if (userId == null) return AjaxResult.error(401, "未登录");
        List<SysMenu> menus = loginService.selectMenuTreeByUserId(userId);
        return AjaxResult.success(loginService.buildMenus(menus));
    }

    @PostMapping("/api/sys-api/logout")
    public AjaxResult logout() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser != null) tokenService.delLoginUser(loginUser.getToken());
        return AjaxResult.success("退出成功");
    }
}
