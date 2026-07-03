package cn.qihangerp.model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
@TableName("o_shop")
public class OShop {
    @TableId private Long id; private String name; private Integer type; private String url;
    private Integer sort; private Integer status; private String remark; private String sellerId;
    private String appKey; private String appSecret; private String accessToken;
    private Long expiresIn; private Long accessTokenBegin; private String refreshToken;
    private Long refreshTokenTimeout; private String apiRequestUrl; private String apiCallbackUrl;
    private Long manageUserId; private Long shopGroupId; private Long regionId;
    private Long merchantId; private Date createTime; private String createBy;
    private Date updateTime; private String updateBy; private Integer apiStatus;
    private String province; private String city; private String district; private String address;
    private String contact; private String phone; private String sellerNum;
    private Integer allowInventoryShare;
    public Long getId() { return id; } public void setId(Long v) { this.id = v; }
    public String getName() { return name; } public void setName(String v) { this.name = v; }
    public Integer getType() { return type; } public void setType(Integer v) { this.type = v; }
    public String getUrl() { return url; } public void setUrl(String v) { this.url = v; }
    public Integer getSort() { return sort; } public void setSort(Integer v) { this.sort = v; }
    public Integer getStatus() { return status; } public void setStatus(Integer v) { this.status = v; }
    public String getRemark() { return remark; } public void setRemark(String v) { this.remark = v; }
    public String getSellerId() { return sellerId; } public void setSellerId(String v) { this.sellerId = v; }
    public String getAppKey() { return appKey; } public void setAppKey(String v) { this.appKey = v; }
    public String getAppSecret() { return appSecret; } public void setAppSecret(String v) { this.appSecret = v; }
    public Long getMerchantId() { return merchantId; } public void setMerchantId(Long v) { this.merchantId = v; }
    public Date getCreateTime() { return createTime; } public void setCreateTime(Date v) { this.createTime = v; }
    public String getCreateBy() { return createBy; } public void setCreateBy(String v) { this.createBy = v; }
    public String getContact() { return contact; } public void setContact(String v) { this.contact = v; }
    public String getPhone() { return phone; } public void setPhone(String v) { this.phone = v; }
}