package cn.qihangerp.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;



import java.util.Date;

public class SysDictData {
    @TableId
    private Long dictCode; private Integer dictSort; private String dictLabel;
    private String dictValue; private String dictType; private String cssClass;
    private String listClass; private String isDefault; private String status;
    private String createBy; private Date createTime; private String updateBy;
    private Date updateTime; private String remark;

    public Long getDictCode() { return dictCode; } public void setDictCode(Long v) { this.dictCode = v; }
    public Integer getDictSort() { return dictSort; } public void setDictSort(Integer v) { this.dictSort = v; }
    public String getDictLabel() { return dictLabel; } public void setDictLabel(String v) { this.dictLabel = v; }
    public String getDictValue() { return dictValue; } public void setDictValue(String v) { this.dictValue = v; }
    public String getDictType() { return dictType; } public void setDictType(String v) { this.dictType = v; }
    public String getCssClass() { return cssClass; } public void setCssClass(String v) { this.cssClass = v; }
    public String getListClass() { return listClass; } public void setListClass(String v) { this.listClass = v; }
    public String getIsDefault() { return isDefault; } public void setIsDefault(String v) { this.isDefault = v; }
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
    public String getCreateBy() { return createBy; } public void setCreateBy(String v) { this.createBy = v; }
    public Date getCreateTime() { return createTime; } public void setCreateTime(Date v) { this.createTime = v; }
    public String getUpdateBy() { return updateBy; } public void setUpdateBy(String v) { this.updateBy = v; }
    public Date getUpdateTime() { return updateTime; } public void setUpdateTime(Date v) { this.updateTime = v; }
    public String getRemark() { return remark; } public void setRemark(String v) { this.remark = v; }
}
