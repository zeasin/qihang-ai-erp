package cn.qihangerp.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

@TableName("erp_stock_out")
public class ErpStockOut {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String outNum;
    private String sourceNum;
    private Long sourceId;
    private Integer type;
    private Long shopId;
    private Integer goodsUnit;
    private Integer specUnit;
    private Integer specUnitTotal;
    private Integer outTotal;
    private String remark;
    private Integer status;
    private Date outTime;
    private Date completeTime;
    private Long operatorId;
    private String operatorName;
    private Date createTime;
    private String createBy;
    private Date updateTime;
    private String updateBy;
    private Long merchantId;
    private Long warehouseId;
    private String warehouseName;

    public Long getId() { return id; } public void setId(Long v) { this.id = v; }
    public String getOutNum() { return outNum; } public void setOutNum(String v) { this.outNum = v; }
    public String getSourceNum() { return sourceNum; } public void setSourceNum(String v) { this.sourceNum = v; }
    public Long getSourceId() { return sourceId; } public void setSourceId(Long v) { this.sourceId = v; }
    public Integer getType() { return type; } public void setType(Integer v) { this.type = v; }
    public Long getShopId() { return shopId; } public void setShopId(Long v) { this.shopId = v; }
    public Integer getGoodsUnit() { return goodsUnit; } public void setGoodsUnit(Integer v) { this.goodsUnit = v; }
    public Integer getSpecUnit() { return specUnit; } public void setSpecUnit(Integer v) { this.specUnit = v; }
    public Integer getSpecUnitTotal() { return specUnitTotal; } public void setSpecUnitTotal(Integer v) { this.specUnitTotal = v; }
    public Integer getOutTotal() { return outTotal; } public void setOutTotal(Integer v) { this.outTotal = v; }
    public String getRemark() { return remark; } public void setRemark(String v) { this.remark = v; }
    public Integer getStatus() { return status; } public void setStatus(Integer v) { this.status = v; }
    public Date getOutTime() { return outTime; } public void setOutTime(Date v) { this.outTime = v; }
    public Date getCompleteTime() { return completeTime; } public void setCompleteTime(Date v) { this.completeTime = v; }
    public Long getOperatorId() { return operatorId; } public void setOperatorId(Long v) { this.operatorId = v; }
    public String getOperatorName() { return operatorName; } public void setOperatorName(String v) { this.operatorName = v; }
    public Date getCreateTime() { return createTime; } public void setCreateTime(Date v) { this.createTime = v; }
    public String getCreateBy() { return createBy; } public void setCreateBy(String v) { this.createBy = v; }
    public Date getUpdateTime() { return updateTime; } public void setUpdateTime(Date v) { this.updateTime = v; }
    public String getUpdateBy() { return updateBy; } public void setUpdateBy(String v) { this.updateBy = v; }
    public Long getMerchantId() { return merchantId; } public void setMerchantId(Long v) { this.merchantId = v; }
    public Long getWarehouseId() { return warehouseId; } public void setWarehouseId(Long v) { this.warehouseId = v; }
    public String getWarehouseName() { return warehouseName; } public void setWarehouseName(String v) { this.warehouseName = v; }
}