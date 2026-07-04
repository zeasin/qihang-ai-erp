package cn.qihangerp.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

/** 国家地区表 o_shop_region */
@TableName("o_shop_region")
public class ShopRegion {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private Double exchangeRate;
    private String num;
    private Integer status;
    private Date createTime;
    private String createBy;
    private Date updateTime;
    private String updateBy;

    public Long getId() { return id; } public void setId(Long v) { this.id = v; }
    public String getName() { return name; } public void setName(String v) { this.name = v; }
    public Double getExchangeRate() { return exchangeRate; } public void setExchangeRate(Double v) { this.exchangeRate = v; }
    public String getNum() { return num; } public void setNum(String v) { this.num = v; }
    public Integer getStatus() { return status; } public void setStatus(Integer v) { this.status = v; }
    public Date getCreateTime() { return createTime; } public void setCreateTime(Date v) { this.createTime = v; }
    public String getCreateBy() { return createBy; } public void setCreateBy(String v) { this.createBy = v; }
    public Date getUpdateTime() { return updateTime; } public void setUpdateTime(Date v) { this.updateTime = v; }
    public String getUpdateBy() { return updateBy; } public void setUpdateBy(String v) { this.updateBy = v; }
}