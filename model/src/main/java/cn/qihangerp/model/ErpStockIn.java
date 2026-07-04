package cn.qihangerp.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.util.List;

@TableName("erp_stock_in")
public class ErpStockIn {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Long id;
    private String inNum;
    private Integer type;
    private String sourceNo;
    private Long sourceId;
    private Integer goodsUnit;
    private Integer specUnit;
    private Integer specUnitTotal;
    private Integer inTotal;
    private String remark;
    private Integer status;
    private Date inTime;
    private Date completeTime;
    private Long operatorId;
    private String operatorName;
    private Date createTime;
    private Date updateTime;
    private Long warehouseId;
    private String warehouseName;

    private List<ErpStockInItem> itemList;

    public Long getId() { return id; } public void setId(Long v) { this.id = v; }
    public String getInNum() { return inNum; } public void setInNum(String v) { this.inNum = v; }
    public Integer getType() { return type; } public void setType(Integer v) { this.type = v; }
    public String getSourceNo() { return sourceNo; } public void setSourceNo(String v) { this.sourceNo = v; }
    public Long getSourceId() { return sourceId; } public void setSourceId(Long v) { this.sourceId = v; }
    public Integer getGoodsUnit() { return goodsUnit; } public void setGoodsUnit(Integer v) { this.goodsUnit = v; }
    public Integer getSpecUnit() { return specUnit; } public void setSpecUnit(Integer v) { this.specUnit = v; }
    public Integer getSpecUnitTotal() { return specUnitTotal; } public void setSpecUnitTotal(Integer v) { this.specUnitTotal = v; }
    public Integer getInTotal() { return inTotal; } public void setInTotal(Integer v) { this.inTotal = v; }
    public String getRemark() { return remark; } public void setRemark(String v) { this.remark = v; }
    public Integer getStatus() { return status; } public void setStatus(Integer v) { this.status = v; }
    public Date getInTime() { return inTime; } public void setInTime(Date v) { this.inTime = v; }
    public Date getCompleteTime() { return completeTime; } public void setCompleteTime(Date v) { this.completeTime = v; }
    public Long getOperatorId() { return operatorId; } public void setOperatorId(Long v) { this.operatorId = v; }
    public String getOperatorName() { return operatorName; } public void setOperatorName(String v) { this.operatorName = v; }
    public Date getCreateTime() { return createTime; } public void setCreateTime(Date v) { this.createTime = v; }
    public Date getUpdateTime() { return updateTime; } public void setUpdateTime(Date v) { this.updateTime = v; }
    public Long getWarehouseId() { return warehouseId; } public void setWarehouseId(Long v) { this.warehouseId = v; }
    public String getWarehouseName() { return warehouseName; } public void setWarehouseName(String v) { this.warehouseName = v; }
    public List<ErpStockInItem> getItemList() { return itemList; } public void setItemList(List<ErpStockInItem> v) { this.itemList = v; }
}