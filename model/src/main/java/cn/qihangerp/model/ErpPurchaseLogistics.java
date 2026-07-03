package cn.qihangerp.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/** 采购承运商表 erp_purchase_logistics */
@TableName("erp_purchase_logistics")
public class ErpPurchaseLogistics {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Long id;
    private String code;
    private String name;
    private String remark;
    private Integer status;
    private Long merchantId;
    private Long shopId;

    public Long getId() { return id; } public void setId(Long v) { this.id = v; }
    public String getCode() { return code; } public void setCode(String v) { this.code = v; }
    public String getName() { return name; } public void setName(String v) { this.name = v; }
    public String getRemark() { return remark; } public void setRemark(String v) { this.remark = v; }
    public Integer getStatus() { return status; } public void setStatus(Integer v) { this.status = v; }
    public Long getMerchantId() { return merchantId; } public void setMerchantId(Long v) { this.merchantId = v; }
    public Long getShopId() { return shopId; } public void setShopId(Long v) { this.shopId = v; }
}