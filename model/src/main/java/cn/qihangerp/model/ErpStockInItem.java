package cn.qihangerp.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

@TableName("erp_stock_in_item")
public class ErpStockInItem {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long stockInId;
    private Integer stockInType;
    private String sourceNo;
    private Long sourceId;
    private Long sourceItemId;
    private Long goodsId;
    private String goodsNum;
    private String goodsName;
    private String goodsImage;
    private Long skuId;
    private String skuCode;
    private String skuName;
    private Integer quantity;
    private Integer inQuantity;
    private String remark;
    private Integer status;
    private Date createTime;
    private Date updateTime;
    private Long warehouseId;

    public Long getId() { return id; } public void setId(Long v) { this.id = v; }
    public Long getStockInId() { return stockInId; } public void setStockInId(Long v) { this.stockInId = v; }
    public Integer getStockInType() { return stockInType; } public void setStockInType(Integer v) { this.stockInType = v; }
    public String getSourceNo() { return sourceNo; } public void setSourceNo(String v) { this.sourceNo = v; }
    public Long getSourceId() { return sourceId; } public void setSourceId(Long v) { this.sourceId = v; }
    public Long getSourceItemId() { return sourceItemId; } public void setSourceItemId(Long v) { this.sourceItemId = v; }
    public Long getGoodsId() { return goodsId; } public void setGoodsId(Long v) { this.goodsId = v; }
    public String getGoodsNum() { return goodsNum; } public void setGoodsNum(String v) { this.goodsNum = v; }
    public String getGoodsName() { return goodsName; } public void setGoodsName(String v) { this.goodsName = v; }
    public String getGoodsImage() { return goodsImage; } public void setGoodsImage(String v) { this.goodsImage = v; }
    public Long getSkuId() { return skuId; } public void setSkuId(Long v) { this.skuId = v; }
    public String getSkuCode() { return skuCode; } public void setSkuCode(String v) { this.skuCode = v; }
    public String getSkuName() { return skuName; } public void setSkuName(String v) { this.skuName = v; }
    public Integer getQuantity() { return quantity; } public void setQuantity(Integer v) { this.quantity = v; }
    public Integer getInQuantity() { return inQuantity; } public void setInQuantity(Integer v) { this.inQuantity = v; }
    public String getRemark() { return remark; } public void setRemark(String v) { this.remark = v; }
    public Integer getStatus() { return status; } public void setStatus(Integer v) { this.status = v; }
    public Date getCreateTime() { return createTime; } public void setCreateTime(Date v) { this.createTime = v; }
    public Date getUpdateTime() { return updateTime; } public void setUpdateTime(Date v) { this.updateTime = v; }
    public Long getWarehouseId() { return warehouseId; } public void setWarehouseId(Long v) { this.warehouseId = v; }
}