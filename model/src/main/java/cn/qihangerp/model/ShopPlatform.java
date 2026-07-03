package cn.qihangerp.model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
@TableName("o_shop_platform")
public class ShopPlatform {
    @TableId private Integer id; private String name; private String code;
    private String appKey; private String appSecret; private String redirectUri;
    private String serverUrl; private Integer status; private Integer sort;
    public Integer getId() { return id; } public void setId(Integer v) { this.id = v; }
    public String getName() { return name; } public void setName(String v) { this.name = v; }
    public String getCode() { return code; } public void setCode(String v) { this.code = v; }
    public String getAppKey() { return appKey; } public void setAppKey(String v) { this.appKey = v; }
    public String getAppSecret() { return appSecret; } public void setAppSecret(String v) { this.appSecret = v; }
    public String getRedirectUri() { return redirectUri; } public void setRedirectUri(String v) { this.redirectUri = v; }
    public String getServerUrl() { return serverUrl; } public void setServerUrl(String v) { this.serverUrl = v; }
    public Integer getStatus() { return status; } public void setStatus(Integer v) { this.status = v; }
    public Integer getSort() { return sort; } public void setSort(Integer v) { this.sort = v; }
}