package cn.qihangerp.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@TableName("erp_purchase_order")
public class ErpPurchaseOrder {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Long id;
    private String orderNum;
    private Long supplierId;
    private String supplierName;
    private BigDecimal totalAmount;
    private Integer goodsUnit;
    private Integer specUnit;
    private Integer specUnitTotal;
    private String remark;
    private Integer status;
    private Date orderTime;
    private Date completeTime;
    private Long userId;
    private String userName;
    private Date createTime;
    private Date updateTime;

    private List<ErpPurchaseOrderItem> itemList;

    public Long getId() { return id; } public void setId(Long v) { this.id = v; }
    public String getOrderNum() { return orderNum; } public void setOrderNum(String v) { this.orderNum = v; }
    public Long getSupplierId() { return supplierId; } public void setSupplierId(Long v) { this.supplierId = v; }
    public String getSupplierName() { return supplierName; } public void setSupplierName(String v) { this.supplierName = v; }
    public BigDecimal getTotalAmount() { return totalAmount; } public void setTotalAmount(BigDecimal v) { this.totalAmount = v; }
    public Integer getGoodsUnit() { return goodsUnit; } public void setGoodsUnit(Integer v) { this.goodsUnit = v; }
    public Integer getSpecUnit() { return specUnit; } public void setSpecUnit(Integer v) { this.specUnit = v; }
    public Integer getSpecUnitTotal() { return specUnitTotal; } public void setSpecUnitTotal(Integer v) { this.specUnitTotal = v; }
    public String getRemark() { return remark; } public void setRemark(String v) { this.remark = v; }
    public Integer getStatus() { return status; } public void setStatus(Integer v) { this.status = v; }
    public Date getOrderTime() { return orderTime; } public void setOrderTime(Date v) { this.orderTime = v; }
    public Date getCompleteTime() { return completeTime; } public void setCompleteTime(Date v) { this.completeTime = v; }
    public Long getUserId() { return userId; } public void setUserId(Long v) { this.userId = v; }
    public String getUserName() { return userName; } public void setUserName(String v) { this.userName = v; }
    public Date getCreateTime() { return createTime; } public void setCreateTime(Date v) { this.createTime = v; }
    public Date getUpdateTime() { return updateTime; } public void setUpdateTime(Date v) { this.updateTime = v; }
    public List<ErpPurchaseOrderItem> getItemList() { return itemList; } public void setItemList(List<ErpPurchaseOrderItem> v) { this.itemList = v; }
}