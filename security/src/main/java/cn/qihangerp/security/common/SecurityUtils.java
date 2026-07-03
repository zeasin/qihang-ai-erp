package cn.qihangerp.security.common;

import cn.qihangerp.security.LoginUser;
import cn.qihangerp.security.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class SecurityUtils {

    private static TokenService tokenService;

    @Autowired
    public void setTokenService(TokenService ts) {
        tokenService = ts;
    }

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attrs != null ? attrs.getRequest() : null;
    }

    public static LoginUser getLoginUser() {
        HttpServletRequest request = getRequest();
        if (request != null && tokenService != null) {
            return tokenService.getLoginUser(request);
        }
        return null;
    }

    public static Long getUserId() {
        LoginUser user = getLoginUser();
        return user != null ? user.getUserId() : null;
    }
}
