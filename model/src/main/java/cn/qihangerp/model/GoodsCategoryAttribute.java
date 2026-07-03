package cn.qihangerp.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/** 分类规格属性表 o_goods_category_attribute */
@TableName("o_goods_category_attribute")
public class GoodsCategoryAttribute {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long categoryId;
    private Integer type;
    private String title;
    private String code;

    public Long getId() { return id; }
    public void setId(Long v) { this.id = v; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long v) { this.categoryId = v; }
    public Integer getType() { return type; }
    public void setType(Integer v) { this.type = v; }
    public String getTitle() { return title; }
    public void setTitle(String v) { this.title = v; }
    public String getCode() { return code; }
    public void setCode(String v) { this.code = v; }
}