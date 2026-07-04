package cn.qihangerp.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/** 快递公司库表 erp_logistics_company */
@TableName("erp_logistics_company")
public class ErpLogisticsCompany {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Long id;
    private Integer platformId;
    private Integer shopId;
    private String logisticsId;
    private String code;
    private String name;
    private String remark;
    private Integer status;
    private Long supplierId;
    private String type;

    public Long getId() { return id; } public void setId(Long v) { this.id = v; }
    public Integer getPlatformId() { return platformId; } public void setPlatformId(Integer v) { this.platformId = v; }
    public Integer getShopId() { return shopId; } public void setShopId(Integer v) { this.shopId = v; }
    public String getLogisticsId() { return logisticsId; } public void setLogisticsId(String v) { this.logisticsId = v; }
    public String getCode() { return code; } public void setCode(String v) { this.code = v; }
    public String getName() { return name; } public void setName(String v) { this.name = v; }
    public String getRemark() { return remark; } public void setRemark(String v) { this.remark = v; }
    public Integer getStatus() { return status; } public void setStatus(Integer v) { this.status = v; }
    public Long getSupplierId() { return supplierId; } public void setSupplierId(Long v) { this.supplierId = v; }
    public String getType() { return type; } public void setType(String v) { this.type = v; }
}