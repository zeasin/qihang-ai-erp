package cn.qihangerp.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;

@TableName("erp_after_sale_item")
public class ErpAfterSaleItem {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long afterSaleId;
    private String afterSaleNum;
    private Long orderItemId;
    private Long goodsId;
    private String goodsNum;
    private String goodsName;
    private String goodsImage;
    private Long skuId;
    private String skuCode;
    private String skuName;
    private Integer quantity;
    private Integer returnQuantity;
    private BigDecimal refundAmount;
    private Integer status;
    private Date createTime;
    private Date updateTime;

    public Long getId() { return id; } public void setId(Long v) { this.id = v; }
    public Long getAfterSaleId() { return afterSaleId; } public void setAfterSaleId(Long v) { this.afterSaleId = v; }
    public String getAfterSaleNum() { return afterSaleNum; } public void setAfterSaleNum(String v) { this.afterSaleNum = v; }
    public Long getOrderItemId() { return orderItemId; } public void setOrderItemId(Long v) { this.orderItemId = v; }
    public Long getGoodsId() { return goodsId; } public void setGoodsId(Long v) { this.goodsId = v; }
    public String getGoodsNum() { return goodsNum; } public void setGoodsNum(String v) { this.goodsNum = v; }
    public String getGoodsName() { return goodsName; } public void setGoodsName(String v) { this.goodsName = v; }
    public String getGoodsImage() { return goodsImage; } public void setGoodsImage(String v) { this.goodsImage = v; }
    public Long getSkuId() { return skuId; } public void setSkuId(Long v) { this.skuId = v; }
    public String getSkuCode() { return skuCode; } public void setSkuCode(String v) { this.skuCode = v; }
    public String getSkuName() { return skuName; } public void setSkuName(String v) { this.skuName = v; }
    public Integer getQuantity() { return quantity; } public void setQuantity(Integer v) { this.quantity = v; }
    public Integer getReturnQuantity() { return returnQuantity; } public void setReturnQuantity(Integer v) { this.returnQuantity = v; }
    public BigDecimal getRefundAmount() { return refundAmount; } public void setRefundAmount(BigDecimal v) { this.refundAmount = v; }
    public Integer getStatus() { return status; } public void setStatus(Integer v) { this.status = v; }
    public Date getCreateTime() { return createTime; } public void setCreateTime(Date v) { this.createTime = v; }
    public Date getUpdateTime() { return updateTime; } public void setUpdateTime(Date v) { this.updateTime = v; }
}