package cn.qihangerp.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;

@TableName("o_goods_sku")
public class OGoodsSku {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long goodsId;
    private String goodsName;
    private String goodsNum;
    private String skuName;
    private String skuCode;
    private Long colorId;
    private String colorValue;
    private Long sizeId;
    private String sizeValue;
    private Long styleId;
    private String styleValue;
    private String barCode;
    private BigDecimal retailPrice;
    private BigDecimal unitCost;
    private String remark;
    private Integer status;
    private BigDecimal weight;
    private Long merchantId;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;

    public Long getId() { return id; }
    public void setId(Long v) { this.id = v; }
    public Long getGoodsId() { return goodsId; }
    public void setGoodsId(Long v) { this.goodsId = v; }
    public String getGoodsName() { return goodsName; }
    public void setGoodsName(String v) { this.goodsName = v; }
    public String getGoodsNum() { return goodsNum; }
    public void setGoodsNum(String v) { this.goodsNum = v; }
    public String getSkuName() { return skuName; }
    public void setSkuName(String v) { this.skuName = v; }
    public String getSkuCode() { return skuCode; }
    public void setSkuCode(String v) { this.skuCode = v; }
    public Long getColorId() { return colorId; }
    public void setColorId(Long v) { this.colorId = v; }
    public String getColorValue() { return colorValue; }
    public void setColorValue(String v) { this.colorValue = v; }
    public Long getSizeId() { return sizeId; }
    public void setSizeId(Long v) { this.sizeId = v; }
    public String getSizeValue() { return sizeValue; }
    public void setSizeValue(String v) { this.sizeValue = v; }
    public Long getStyleId() { return styleId; }
    public void setStyleId(Long v) { this.styleId = v; }
    public String getStyleValue() { return styleValue; }
    public void setStyleValue(String v) { this.styleValue = v; }
    public String getBarCode() { return barCode; }
    public void setBarCode(String v) { this.barCode = v; }
    public BigDecimal getRetailPrice() { return retailPrice; }
    public void setRetailPrice(BigDecimal v) { this.retailPrice = v; }
    public BigDecimal getUnitCost() { return unitCost; }
    public void setUnitCost(BigDecimal v) { this.unitCost = v; }
    public String getRemark() { return remark; }
    public void setRemark(String v) { this.remark = v; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer v) { this.status = v; }
    public BigDecimal getWeight() { return weight; }
    public void setWeight(BigDecimal v) { this.weight = v; }
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
}
