package cn.qihangerp.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

/** 供应商表 erp_supplier */
@TableName("erp_supplier")
public class ErpSupplier {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String number;
    private Integer isShipper;
    private Long warehouseId;
    private String linkMan;
    private String contact;
    private String province;
    private String city;
    private String county;
    private String address;
    private String usci;
    private String bl;
    private String blPeriod;
    private String blFaren;
    private String bank;
    private String bankAccountName;
    private String bankAccount;
    private String purchaserName;
    private String remark;
    private Integer disable;
    private Integer isDelete;
    private Long merchantId;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;

    public Long getId() { return id; } public void setId(Long v) { this.id = v; }
    public String getName() { return name; } public void setName(String v) { this.name = v; }
    public String getNumber() { return number; } public void setNumber(String v) { this.number = v; }
    public Integer getIsShipper() { return isShipper; } public void setIsShipper(Integer v) { this.isShipper = v; }
    public Long getWarehouseId() { return warehouseId; } public void setWarehouseId(Long v) { this.warehouseId = v; }
    public String getLinkMan() { return linkMan; } public void setLinkMan(String v) { this.linkMan = v; }
    public String getContact() { return contact; } public void setContact(String v) { this.contact = v; }
    public String getProvince() { return province; } public void setProvince(String v) { this.province = v; }
    public String getCity() { return city; } public void setCity(String v) { this.city = v; }
    public String getCounty() { return county; } public void setCounty(String v) { this.county = v; }
    public String getAddress() { return address; } public void setAddress(String v) { this.address = v; }
    public String getUsci() { return usci; } public void setUsci(String v) { this.usci = v; }
    public String getBl() { return bl; } public void setBl(String v) { this.bl = v; }
    public String getBlPeriod() { return blPeriod; } public void setBlPeriod(String v) { this.blPeriod = v; }
    public String getBlFaren() { return blFaren; } public void setBlFaren(String v) { this.blFaren = v; }
    public String getBank() { return bank; } public void setBank(String v) { this.bank = v; }
    public String getBankAccountName() { return bankAccountName; } public void setBankAccountName(String v) { this.bankAccountName = v; }
    public String getBankAccount() { return bankAccount; } public void setBankAccount(String v) { this.bankAccount = v; }
    public String getPurchaserName() { return purchaserName; } public void setPurchaserName(String v) { this.purchaserName = v; }
    public String getRemark() { return remark; } public void setRemark(String v) { this.remark = v; }
    public Integer getDisable() { return disable; } public void setDisable(Integer v) { this.disable = v; }
    public Integer getIsDelete() { return isDelete; } public void setIsDelete(Integer v) { this.isDelete = v; }
    public Long getMerchantId() { return merchantId; } public void setMerchantId(Long v) { this.merchantId = v; }
    public String getCreateBy() { return createBy; } public void setCreateBy(String v) { this.createBy = v; }
    public Date getCreateTime() { return createTime; } public void setCreateTime(Date v) { this.createTime = v; }
    public String getUpdateBy() { return updateBy; } public void setUpdateBy(String v) { this.updateBy = v; }
    public Date getUpdateTime() { return updateTime; } public void setUpdateTime(Date v) { this.updateTime = v; }
}