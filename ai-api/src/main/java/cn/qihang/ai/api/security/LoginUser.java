package cn.qihang.ai.api.security;

import cn.qihang.ai.business.model.SysRole;
import cn.qihang.ai.business.model.SysUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LoginUser implements UserDetails {
    private static final ObjectMapper mapper = new ObjectMapper();

    private Long userId;
    private String token;
    private Long deptId;
    private Long loginTime;
    private Long expireTime;
    private List<SysRole> roles;
    private Set<String> permissions;
    private SysUser user;

    public LoginUser() {}
    public LoginUser(SysUser user, List<SysRole> roles, Set<String> permissions) {
        this.user = user; this.userId = user != null ? user.getUserId() : null;
        this.deptId = user != null ? user.getDeptId() : null;
        this.roles = roles; this.permissions = permissions;
    }

    public String toJson() {
        try { return mapper.writeValueAsString(this); }
        catch (JsonProcessingException e) { return "{}"; }
    }
    public static LoginUser fromJson(String json) {
        try { return mapper.readValue(json, LoginUser.class); }
        catch (Exception e) { return null; }
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long v) { this.userId = v; }
    public String getToken() { return token; }
    public void setToken(String v) { this.token = v; }
    public Long getLoginTime() { return loginTime; }
    public void setLoginTime(Long v) { this.loginTime = v; }
    public Long getExpireTime() { return expireTime; }
    public void setExpireTime(Long v) { this.expireTime = v; }
    public List<SysRole> getRoles() { return roles; }
    public void setRoles(List<SysRole> v) { this.roles = v; }
    public Set<String> getPermissions() { return permissions; }
    public void setPermissions(Set<String> v) { this.permissions = v; }
    public SysUser getUser() { return user; }
    public void setUser(SysUser v) { this.user = v; }
    public Long getDeptId() { return user != null ? user.getDeptId() : null; }

    @JsonIgnore @Override public String getPassword() { return user != null ? user.getPassword() : null; }
    @JsonIgnore @Override public String getUsername() { return user != null ? user.getUserName() : null; }
    @JsonIgnore @Override public boolean isAccountNonExpired() { return true; }
    @JsonIgnore @Override public boolean isAccountNonLocked() { return true; }
    @JsonIgnore @Override public boolean isCredentialsNonExpired() { return true; }
    @JsonIgnore @Override public boolean isEnabled() { return user != null && "0".equals(user.getStatus()); }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (permissions == null || permissions.isEmpty()) return Collections.emptyList();
        return permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
