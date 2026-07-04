package cn.qihangerp.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

@TableName("o_goods_sku_stock")
public class OGoodsSkuStock {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long skuId;
    private Long goodsId;
    private String skuCode;
    private String goodsName;
    private String goodsImg;
    private String skuName;
    private Integer quantity;
    private Date createTime;
    private Date updateTime;

    public Long getId() { return id; } public void setId(Long v) { this.id = v; }
    public Long getSkuId() { return skuId; } public void setSkuId(Long v) { this.skuId = v; }
    public Long getGoodsId() { return goodsId; } public void setGoodsId(Long v) { this.goodsId = v; }
    public String getSkuCode() { return skuCode; } public void setSkuCode(String v) { this.skuCode = v; }
    public String getGoodsName() { return goodsName; } public void setGoodsName(String v) { this.goodsName = v; }
    public String getGoodsImg() { return goodsImg; } public void setGoodsImg(String v) { this.goodsImg = v; }
    public String getSkuName() { return skuName; } public void setSkuName(String v) { this.skuName = v; }
    public Integer getQuantity() { return quantity; } public void setQuantity(Integer v) { this.quantity = v; }
    public Date getCreateTime() { return createTime; } public void setCreateTime(Date v) { this.createTime = v; }
    public Date getUpdateTime() { return updateTime; } public void setUpdateTime(Date v) { this.updateTime = v; }
}