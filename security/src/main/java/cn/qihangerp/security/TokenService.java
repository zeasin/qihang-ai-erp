package cn.qihangerp.security;

import cn.qihangerp.security.common.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class TokenService {

    @Value("${token.secret:qihang-ai-erp-jwt-secret-2026！！}")
    private String secret;

    @Value("${token.expireTime:480}")
    private int expireTime;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public LoginUser getLoginUser(HttpServletRequest request) {
        String token = getToken(request);
        System.out.println("[TokenService] token: " + (token != null ? token.substring(0, Math.min(30, token.length())) + "..." : "null"));
        if (token != null && !token.isEmpty()) {
            try {
                Claims claims = parseToken(token);
                String uuid = claims.get(Constants.LOGIN_USER_KEY, String.class);
                System.out.println("[TokenService] uuid: " + uuid);
                String redisKey = Constants.LOGIN_TOKEN_KEY + uuid;
                String json = redisTemplate.opsForValue().get(redisKey);
                System.out.println("[TokenService] redisKey: " + redisKey + ", found: " + (json != null));
                if (json != null) {
                    LoginUser user = LoginUser.fromJson(json);
                    System.out.println("[TokenService] user: " + (user != null ? user.getUsername() : "deserialize null"));
                    return user;
                }
            } catch (Exception e) {
                System.out.println("[TokenService] ERROR: " + e.getClass().getName() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }

    public void delLoginUser(String token) {
        if (token != null && !token.isEmpty()) {
            redisTemplate.delete(Constants.LOGIN_TOKEN_KEY + token);
        }
    }

    public String createToken(LoginUser loginUser) {
        String tokenValue = UUID.randomUUID().toString().replace("-", "");
        loginUser.setToken(tokenValue);
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * 60 * 1000L);
        redisTemplate.opsForValue().set(
            Constants.LOGIN_TOKEN_KEY + tokenValue,
            loginUser.toJson(),
            expireTime, TimeUnit.MINUTES);
        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.LOGIN_USER_KEY, tokenValue);
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
            .claims(claims)
            .signWith(key)
            .compact();
    }

    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * 60 * 1000L);
        redisTemplate.opsForValue().set(
            Constants.LOGIN_TOKEN_KEY + loginUser.getToken(),
            loginUser.toJson(),
            expireTime, TimeUnit.MINUTES);
    }

    private Claims parseToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    private String getToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) token = request.getParameter("token");
        if (token != null && token.startsWith(Constants.TOKEN_PREFIX))
            token = token.substring(Constants.TOKEN_PREFIX.length());
        return token;
    }
}
