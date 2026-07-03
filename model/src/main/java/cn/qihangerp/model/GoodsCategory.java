package cn.qihangerp.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

/** 商品分类表 o_goods_category */
@TableName("o_goods_category")
public class GoodsCategory {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private String number;
    private String name;
    private String remark;
    private Long parentId;
    private String path;
    private Integer sort;
    private String image;
    private Integer isDelete;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
    private Long merchantId;

    public Long getId() { return id; }
    public void setId(Long v) { this.id = v; }
    public String getNumber() { return number; }
    public void setNumber(String v) { this.number = v; }
    public String getName() { return name; }
    public void setName(String v) { this.name = v; }
    public String getRemark() { return remark; }
    public void setRemark(String v) { this.remark = v; }
    public Long getParentId() { return parentId; }
    public void setParentId(Long v) { this.parentId = v; }
    public String getPath() { return path; }
    public void setPath(String v) { this.path = v; }
    public Integer getSort() { return sort; }
    public void setSort(Integer v) { this.sort = v; }
    public String getImage() { return image; }
    public void setImage(String v) { this.image = v; }
    public Integer getIsDelete() { return isDelete; }
    public void setIsDelete(Integer v) { this.isDelete = v; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String v) { this.createBy = v; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date v) { this.createTime = v; }
    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String v) { this.updateBy = v; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date v) { this.updateTime = v; }
    public Long getMerchantId() { return merchantId; }
    public void setMerchantId(Long v) { this.merchantId = v; }
}