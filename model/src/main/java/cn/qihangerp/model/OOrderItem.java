package cn.qihangerp.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;

@TableName("o_order_item")
public class OOrderItem {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long orderId;
    private String orderNum;
    private Long goodsId;
    private Long goodsSkuId;
    private String goodsTitle;
    private String goodsImg;
    private String goodsNum;
    private String goodsSpec;
    private String skuNum;
    private BigDecimal goodsPrice;
    private Integer quantity;
    private BigDecimal itemAmount;
    private BigDecimal discountAmount;
    private BigDecimal payment;
    private String remark;
    private Integer refundCount;
    private Integer refundStatus;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;

    public Long getId() { return id; }
    public void setId(Long v) { this.id = v; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long v) { this.orderId = v; }
    public String getOrderNum() { return orderNum; }
    public void setOrderNum(String v) { this.orderNum = v; }
    public Long getGoodsId() { return goodsId; }
    public void setGoodsId(Long v) { this.goodsId = v; }
    public Long getGoodsSkuId() { return goodsSkuId; }
    public void setGoodsSkuId(Long v) { this.goodsSkuId = v; }
    public String getGoodsTitle() { return goodsTitle; }
    public void setGoodsTitle(String v) { this.goodsTitle = v; }
    public String getGoodsImg() { return goodsImg; }
    public void setGoodsImg(String v) { this.goodsImg = v; }
    public String getGoodsNum() { return goodsNum; }
    public void setGoodsNum(String v) { this.goodsNum = v; }
    public String getGoodsSpec() { return goodsSpec; }
    public void setGoodsSpec(String v) { this.goodsSpec = v; }
    public String getSkuNum() { return skuNum; }
    public void setSkuNum(String v) { this.skuNum = v; }
    public BigDecimal getGoodsPrice() { return goodsPrice; }
    public void setGoodsPrice(BigDecimal v) { this.goodsPrice = v; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer v) { this.quantity = v; }
    public BigDecimal getItemAmount() { return itemAmount; }
    public void setItemAmount(BigDecimal v) { this.itemAmount = v; }
    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal v) { this.discountAmount = v; }
    public BigDecimal getPayment() { return payment; }
    public void setPayment(BigDecimal v) { this.payment = v; }
    public String getRemark() { return remark; }
    public void setRemark(String v) { this.remark = v; }
    public Integer getRefundCount() { return refundCount; }
    public void setRefundCount(Integer v) { this.refundCount = v; }
    public Integer getRefundStatus() { return refundStatus; }
    public void setRefundStatus(Integer v) { this.refundStatus = v; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String v) { this.createBy = v; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date v) { this.createTime = v; }
    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String v) { this.updateBy = v; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date v) { this.updateTime = v; }
}