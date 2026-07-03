package cn.qihangerp.model;

public class LoginBody {
    private String username;
    private String password;
    private String code;
    private String uuid;

    public String getUsername() {
        return username;
    }

    public void setUsername(String v) {
        this.username = v;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String v) {
        this.password = v;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String v) {
        this.code = v;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String v) {
        this.uuid = v;
    }
}