package cn.qihangerp.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

@TableName("o_goods_stock_log")
public class OGoodsStockLog {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long skuId;
    private Long goodsId;
    private String skuCode;
    private String goodsName;
    private String skuName;
    private Integer beforeQuantity;
    private Integer changeQuantity;
    private Integer afterQuantity;
    private Integer type;
    private String remark;
    private String sourceNo;
    private Long warehouseId;
    private String warehouseName;
    private String createBy;
    private Date createTime;

    public Long getId() { return id; } public void setId(Long v) { this.id = v; }
    public Long getSkuId() { return skuId; } public void setSkuId(Long v) { this.skuId = v; }
    public Long getGoodsId() { return goodsId; } public void setGoodsId(Long v) { this.goodsId = v; }
    public String getSkuCode() { return skuCode; } public void setSkuCode(String v) { this.skuCode = v; }
    public String getGoodsName() { return goodsName; } public void setGoodsName(String v) { this.goodsName = v; }
    public String getSkuName() { return skuName; } public void setSkuName(String v) { this.skuName = v; }
    public Integer getBeforeQuantity() { return beforeQuantity; } public void setBeforeQuantity(Integer v) { this.beforeQuantity = v; }
    public Integer getChangeQuantity() { return changeQuantity; } public void setChangeQuantity(Integer v) { this.changeQuantity = v; }
    public Integer getAfterQuantity() { return afterQuantity; } public void setAfterQuantity(Integer v) { this.afterQuantity = v; }
    public Integer getType() { return type; } public void setType(Integer v) { this.type = v; }
    public String getRemark() { return remark; } public void setRemark(String v) { this.remark = v; }
    public String getSourceNo() { return sourceNo; } public void setSourceNo(String v) { this.sourceNo = v; }
    public Long getWarehouseId() { return warehouseId; } public void setWarehouseId(Long v) { this.warehouseId = v; }
    public String getWarehouseName() { return warehouseName; } public void setWarehouseName(String v) { this.warehouseName = v; }
    public String getCreateBy() { return createBy; } public void setCreateBy(String v) { this.createBy = v; }
    public Date getCreateTime() { return createTime; } public void setCreateTime(Date v) { this.createTime = v; }
}