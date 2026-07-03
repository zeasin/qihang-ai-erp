package cn.qihangerp.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

/** 仓库表 erp_warehouse */
@TableName("erp_warehouse")
public class ErpWarehouse {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Long id;
    private String warehouseNo;
    private String warehouseName;
    private Integer type;
    private String status;
    private String province;
    private String city;
    private String county;
    private String address;
    private String contacts;
    private String phone;
    private String remark;
    private String warehouseType;
    private Long merchantId;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;

    public Long getId() { return id; } public void setId(Long v) { this.id = v; }
    public String getWarehouseNo() { return warehouseNo; } public void setWarehouseNo(String v) { this.warehouseNo = v; }
    public String getWarehouseName() { return warehouseName; } public void setWarehouseName(String v) { this.warehouseName = v; }
    public Integer getType() { return type; } public void setType(Integer v) { this.type = v; }
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
    public String getProvince() { return province; } public void setProvince(String v) { this.province = v; }
    public String getCity() { return city; } public void setCity(String v) { this.city = v; }
    public String getCounty() { return county; } public void setCounty(String v) { this.county = v; }
    public String getAddress() { return address; } public void setAddress(String v) { this.address = v; }
    public String getContacts() { return contacts; } public void setContacts(String v) { this.contacts = v; }
    public String getPhone() { return phone; } public void setPhone(String v) { this.phone = v; }
    public String getRemark() { return remark; } public void setRemark(String v) { this.remark = v; }
    public String getWarehouseType() { return warehouseType; } public void setWarehouseType(String v) { this.warehouseType = v; }
    public Long getMerchantId() { return merchantId; } public void setMerchantId(Long v) { this.merchantId = v; }
    public String getCreateBy() { return createBy; } public void setCreateBy(String v) { this.createBy = v; }
    public Date getCreateTime() { return createTime; } public void setCreateTime(Date v) { this.createTime = v; }
    public String getUpdateBy() { return updateBy; } public void setUpdateBy(String v) { this.updateBy = v; }
    public Date getUpdateTime() { return updateTime; } public void setUpdateTime(Date v) { this.updateTime = v; }
}