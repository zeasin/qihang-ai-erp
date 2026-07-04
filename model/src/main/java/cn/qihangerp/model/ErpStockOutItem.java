package cn.qihangerp.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

@TableName("erp_stock_out_item")
public class ErpStockOutItem {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Integer type;
    private Long entryId;
    private Long sourceOrderId;
    private Long sourceOrderItemId;
    private String sourceOrderNum;
    private Long originalQuantity;
    private Long outQuantity;
    private Date completeTime;
    private Date pickedTime;
    private Integer status;
    private Long batchId;
    private Long warehouseId;
    private Long positionId;
    private String positionNum;
    private Long goodsId;
    private String goodsNum;
    private String goodsName;
    private String goodsImage;
    private Long skuId;
    private String skuCode;
    private String skuName;
    private Date createTime;
    private String createBy;
    private Date updateTime;
    private String updateBy;
    private java.math.BigDecimal purPrice;
    private Long shopId;
    private Long shopGroupId;
    private Long merchantId;
    private Integer inventoryMode;

    public Long getId() { return id; } public void setId(Long v) { this.id = v; }
    public Integer getType() { return type; } public void setType(Integer v) { this.type = v; }
    public Long getEntryId() { return entryId; } public void setEntryId(Long v) { this.entryId = v; }
    public Long getSourceOrderId() { return sourceOrderId; } public void setSourceOrderId(Long v) { this.sourceOrderId = v; }
    public Long getSourceOrderItemId() { return sourceOrderItemId; } public void setSourceOrderItemId(Long v) { this.sourceOrderItemId = v; }
    public String getSourceOrderNum() { return sourceOrderNum; } public void setSourceOrderNum(String v) { this.sourceOrderNum = v; }
    public Long getOriginalQuantity() { return originalQuantity; } public void setOriginalQuantity(Long v) { this.originalQuantity = v; }
    public Long getOutQuantity() { return outQuantity; } public void setOutQuantity(Long v) { this.outQuantity = v; }
    public Date getCompleteTime() { return completeTime; } public void setCompleteTime(Date v) { this.completeTime = v; }
    public Date getPickedTime() { return pickedTime; } public void setPickedTime(Date v) { this.pickedTime = v; }
    public Integer getStatus() { return status; } public void setStatus(Integer v) { this.status = v; }
    public Long getBatchId() { return batchId; } public void setBatchId(Long v) { this.batchId = v; }
    public Long getWarehouseId() { return warehouseId; } public void setWarehouseId(Long v) { this.warehouseId = v; }
    public Long getPositionId() { return positionId; } public void setPositionId(Long v) { this.positionId = v; }
    public String getPositionNum() { return positionNum; } public void setPositionNum(String v) { this.positionNum = v; }
    public Long getGoodsId() { return goodsId; } public void setGoodsId(Long v) { this.goodsId = v; }
    public String getGoodsNum() { return goodsNum; } public void setGoodsNum(String v) { this.goodsNum = v; }
    public String getGoodsName() { return goodsName; } public void setGoodsName(String v) { this.goodsName = v; }
    public String getGoodsImage() { return goodsImage; } public void setGoodsImage(String v) { this.goodsImage = v; }
    public Long getSkuId() { return skuId; } public void setSkuId(Long v) { this.skuId = v; }
    public String getSkuCode() { return skuCode; } public void setSkuCode(String v) { this.skuCode = v; }
    public String getSkuName() { return skuName; } public void setSkuName(String v) { this.skuName = v; }
    public Date getCreateTime() { return createTime; } public void setCreateTime(Date v) { this.createTime = v; }
    public String getCreateBy() { return createBy; } public void setCreateBy(String v) { this.createBy = v; }
    public Date getUpdateTime() { return updateTime; } public void setUpdateTime(Date v) { this.updateTime = v; }
    public String getUpdateBy() { return updateBy; } public void setUpdateBy(String v) { this.updateBy = v; }
    public java.math.BigDecimal getPurPrice() { return purPrice; } public void setPurPrice(java.math.BigDecimal v) { this.purPrice = v; }
    public Long getShopId() { return shopId; } public void setShopId(Long v) { this.shopId = v; }
    public Long getShopGroupId() { return shopGroupId; } public void setShopGroupId(Long v) { this.shopGroupId = v; }
    public Long getMerchantId() { return merchantId; } public void setMerchantId(Long v) { this.merchantId = v; }
    public Integer getInventoryMode() { return inventoryMode; } public void setInventoryMode(Integer v) { this.inventoryMode = v; }
}