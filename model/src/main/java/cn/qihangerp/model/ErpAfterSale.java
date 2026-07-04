package cn.qihangerp.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@TableName("erp_after_sale")
public class ErpAfterSale {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String afterNum;
    private Long orderId;
    private String orderNum;
    private Integer type;
    private String reason;
    private BigDecimal amount;
    private Integer status;
    private Date applyTime;
    private Date completeTime;
    private String remark;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
    @TableField(exist = false)
    private List<ErpAfterSaleItem> itemList;

    public Long getId() { return id; } public void setId(Long v) { this.id = v; }
    public String getAfterNum() { return afterNum; } public void setAfterNum(String v) { this.afterNum = v; }
    public Long getOrderId() { return orderId; } public void setOrderId(Long v) { this.orderId = v; }
    public String getOrderNum() { return orderNum; } public void setOrderNum(String v) { this.orderNum = v; }
    public Integer getType() { return type; } public void setType(Integer v) { this.type = v; }
    public String getReason() { return reason; } public void setReason(String v) { this.reason = v; }
    public BigDecimal getAmount() { return amount; } public void setAmount(BigDecimal v) { this.amount = v; }
    public Integer getStatus() { return status; } public void setStatus(Integer v) { this.status = v; }
    public Date getApplyTime() { return applyTime; } public void setApplyTime(Date v) { this.applyTime = v; }
    public Date getCompleteTime() { return completeTime; } public void setCompleteTime(Date v) { this.completeTime = v; }
    public String getRemark() { return remark; } public void setRemark(String v) { this.remark = v; }
    public String getCreateBy() { return createBy; } public void setCreateBy(String v) { this.createBy = v; }
    public Date getCreateTime() { return createTime; } public void setCreateTime(Date v) { this.createTime = v; }
    public String getUpdateBy() { return updateBy; } public void setUpdateBy(String v) { this.updateBy = v; }
    public Date getUpdateTime() { return updateTime; } public void setUpdateTime(Date v) { this.updateTime = v; }
    public List<ErpAfterSaleItem> getItemList() { return itemList; } public void setItemList(List<ErpAfterSaleItem> v) { this.itemList = v; }
}