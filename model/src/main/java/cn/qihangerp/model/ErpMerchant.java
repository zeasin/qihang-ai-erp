package cn.qihangerp.model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
@TableName("erp_merchant")
public class ErpMerchant {
    @TableId private Long id; private String loginName; private String nickName;
    private String mobile; private String password; private String status;
    private String name; private String number; private String usci; private String faren;
    private String linkMan; private String address; private String supplierIds;
    private Date createTime;
    public Long getId() { return id; } public void setId(Long v) { this.id = v; }
    public String getLoginName() { return loginName; } public void setLoginName(String v) { this.loginName = v; }
    public String getNickName() { return nickName; } public void setNickName(String v) { this.nickName = v; }
    public String getMobile() { return mobile; } public void setMobile(String v) { this.mobile = v; }
    public String getName() { return name; } public void setName(String v) { this.name = v; }
    public String getNumber() { return number; } public void setNumber(String v) { this.number = v; }
    public String getUsci() { return usci; } public void setUsci(String v) { this.usci = v; }
    public String getFaren() { return faren; } public void setFaren(String v) { this.faren = v; }
    public String getLinkMan() { return linkMan; } public void setLinkMan(String v) { this.linkMan = v; }
    public String getAddress() { return address; } public void setAddress(String v) { this.address = v; }
    public String getSupplierIds() { return supplierIds; } public void setSupplierIds(String v) { this.supplierIds = v; }
    public Date getCreateTime() { return createTime; } public void setCreateTime(Date v) { this.createTime = v; }
}