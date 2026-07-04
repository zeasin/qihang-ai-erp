package cn.qihangerp.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@TableName("o_order")
public class OOrder {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private String orderNum;
    private Integer orderMode;
    private Integer orderStatus;
    private BigDecimal goodsAmount;
    private BigDecimal amount;
    private BigDecimal payment;
    private BigDecimal postFee;
    private BigDecimal discountAmount;
    private String receiverName;
    private String receiverMobile;
    private String province;
    private String city;
    private String town;
    private String address;
    private String remark;
    private Date orderTime;
    private Long merchantId;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;

    @TableField(exist = false)
    private List<OOrderItem> itemList;

    public Long getId() { return id; }
    public void setId(Long v) { this.id = v; }
    public String getOrderNum() { return orderNum; }
    public void setOrderNum(String v) { this.orderNum = v; }
    public Integer getOrderMode() { return orderMode; }
    public void setOrderMode(Integer v) { this.orderMode = v; }
    public Integer getOrderStatus() { return orderStatus; }
    public void setOrderStatus(Integer v) { this.orderStatus = v; }
    public BigDecimal getGoodsAmount() { return goodsAmount; }
    public void setGoodsAmount(BigDecimal v) { this.goodsAmount = v; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal v) { this.amount = v; }
    public BigDecimal getPayment() { return payment; }
    public void setPayment(BigDecimal v) { this.payment = v; }
    public BigDecimal getPostFee() { return postFee; }
    public void setPostFee(BigDecimal v) { this.postFee = v; }
    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal v) { this.discountAmount = v; }
    public String getReceiverName() { return receiverName; }
    public void setReceiverName(String v) { this.receiverName = v; }
    public String getReceiverMobile() { return receiverMobile; }
    public void setReceiverMobile(String v) { this.receiverMobile = v; }
    public String getProvince() { return province; }
    public void setProvince(String v) { this.province = v; }
    public String getCity() { return city; }
    public void setCity(String v) { this.city = v; }
    public String getTown() { return town; }
    public void setTown(String v) { this.town = v; }
    public String getAddress() { return address; }
    public void setAddress(String v) { this.address = v; }
    public String getRemark() { return remark; }
    public void setRemark(String v) { this.remark = v; }
    public Date getOrderTime() { return orderTime; }
    public void setOrderTime(Date v) { this.orderTime = v; }
    public Long getMerchantId() { return merchantId; }
    public void setMerchantId(Long v) { this.merchantId = v; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String v) { this.createBy = v; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date v) { this.createTime = v; }
    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String v) { this.updateBy = v; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date v) { this.updateTime = v; }
    public List<OOrderItem> getItemList() { return itemList; }
    public void setItemList(List<OOrderItem> v) { this.itemList = v; }
}