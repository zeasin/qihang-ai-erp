package cn.qihangerp.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/** 规格属性值表 o_goods_category_attribute_value */
@TableName("o_goods_category_attribute_value")
public class GoodsCategoryAttributeValue {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long categoryAttributeId;
    private String value;
    private String skuCode;
    private Integer orderNum;
    private Integer isDelete;

    public Long getId() { return id; }
    public void setId(Long v) { this.id = v; }
    public Long getCategoryAttributeId() { return categoryAttributeId; }
    public void setCategoryAttributeId(Long v) { this.categoryAttributeId = v; }
    public String getValue() { return value; }
    public void setValue(String v) { this.value = v; }
    public String getSkuCode() { return skuCode; }
    public void setSkuCode(String v) { this.skuCode = v; }
    public Integer getOrderNum() { return orderNum; }
    public void setOrderNum(Integer v) { this.orderNum = v; }
    public Integer getIsDelete() { return isDelete; }
    public void setIsDelete(Integer v) { this.isDelete = v; }
}