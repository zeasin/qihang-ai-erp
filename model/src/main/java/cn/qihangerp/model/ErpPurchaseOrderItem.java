package cn.qihangerp.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;

@TableName("erp_purchase_order_item")
public class ErpPurchaseOrderItem {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long purchaseOrderId;
    private String purchaseOrderNum;
    private Long goodsId;
    private String goodsNum;
    private String goodsName;
    private String goodsImage;
    private Long skuId;
    private String skuCode;
    private String skuName;
    private Integer quantity;
    private Integer inQuantity;
    private BigDecimal purchasePrice;
    private BigDecimal totalAmount;
    private String remark;
    private Date createTime;
    private Date updateTime;

    public Long getId() { return id; } public void setId(Long v) { this.id = v; }
    public Long getPurchaseOrderId() { return purchaseOrderId; } public void setPurchaseOrderId(Long v) { this.purchaseOrderId = v; }
    public String getPurchaseOrderNum() { return purchaseOrderNum; } public void setPurchaseOrderNum(String v) { this.purchaseOrderNum = v; }
    public Long getGoodsId() { return goodsId; } public void setGoodsId(Long v) { this.goodsId = v; }
    public String getGoodsNum() { return goodsNum; } public void setGoodsNum(String v) { this.goodsNum = v; }
    public String getGoodsName() { return goodsName; } public void setGoodsName(String v) { this.goodsName = v; }
    public String getGoodsImage() { return goodsImage; } public void setGoodsImage(String v) { this.goodsImage = v; }
    public Long getSkuId() { return skuId; } public void setSkuId(Long v) { this.skuId = v; }
    public String getSkuCode() { return skuCode; } public void setSkuCode(String v) { this.skuCode = v; }
    public String getSkuName() { return skuName; } public void setSkuName(String v) { this.skuName = v; }
    public Integer getQuantity() { return quantity; } public void setQuantity(Integer v) { this.quantity = v; }
    public Integer getInQuantity() { return inQuantity; } public void setInQuantity(Integer v) { this.inQuantity = v; }
    public BigDecimal getPurchasePrice() { return purchasePrice; } public void setPurchasePrice(BigDecimal v) { this.purchasePrice = v; }
    public BigDecimal getTotalAmount() { return totalAmount; } public void setTotalAmount(BigDecimal v) { this.totalAmount = v; }
    public String getRemark() { return remark; } public void setRemark(String v) { this.remark = v; }
    public Date getCreateTime() { return createTime; } public void setCreateTime(Date v) { this.createTime = v; }
    public Date getUpdateTime() { return updateTime; } public void setUpdateTime(Date v) { this.updateTime = v; }
}