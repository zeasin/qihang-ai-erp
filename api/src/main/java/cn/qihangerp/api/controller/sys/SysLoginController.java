package cn.qihangerp.api.controller.sys;

import cn.qihangerp.security.LoginUser;
import cn.qihangerp.security.SysLoginService;
import cn.qihangerp.security.TokenService;
import cn.qihangerp.security.common.Constants;
import cn.qihangerp.security.common.SecurityUtils;
import cn.qihangerp.model.LoginBody;
import cn.qihangerp.model.SysMenu;
import cn.qihangerp.common.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
public class SysLoginController {

    @Autowired
    private org.springframework.data.redis.core.StringRedisTemplate redisTemplate;

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

    @GetMapping("/api/sys-api/debug-token")
    public AjaxResult debugToken(HttpServletRequest request) {
        try {
            String rawToken = request.getHeader("Authorization");
            if (rawToken != null && rawToken.startsWith("Bearer ")) {
                rawToken = rawToken.substring(7);
            }
            if (rawToken == null) rawToken = request.getParameter("token");
            
            if (rawToken == null) {
                return AjaxResult.error(400, "no token");
            }
            
            // Manual parse
            var key = io.jsonwebtoken.security.Keys.hmacShaKeyFor(
                "qihangaiersecretkey2026abcdefghijklmnopqrstuvwxyz1234567890".getBytes(java.nio.charset.StandardCharsets.UTF_8));
            var claims = io.jsonwebtoken.Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(rawToken)
                .getPayload();
            
            String uuid = claims.get("login_user_key", String.class);
            String redisKey = "login_tokens:" + uuid;
            String redisVal = redisTemplate.opsForValue().get(redisKey);
            
            java.util.Map<String, Object> result = new java.util.HashMap<>();
            result.put("claims", claims);
            result.put("uuid", uuid);
            result.put("redisKey", redisKey);
            result.put("redisVal", redisVal != null ? redisVal.substring(0, Math.min(100, redisVal.length())) : null);
            
            return AjaxResult.success(result);
        } catch (Exception e) {
            java.util.Map<String, Object> err = new java.util.HashMap<>();
            err.put("error", e.getClass().getName());
            err.put("message", e.getMessage());
            java.io.StringWriter sw = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw));
            err.put("stack", sw.toString().substring(0, 500));
            return AjaxResult.error(500, err.toString());
        }
    }

}
