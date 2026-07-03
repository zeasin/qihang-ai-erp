package cn.qihangerp.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;



import java.util.Date;

public class SysDictType {
    @TableId
    private Long dictId; private String dictName; private String dictType;
    private String status; private String createBy; private Date createTime;
    private String updateBy; private Date updateTime; private String remark;

    public Long getDictId() { return dictId; } public void setDictId(Long v) { this.dictId = v; }
    public String getDictName() { return dictName; } public void setDictName(String v) { this.dictName = v; }
    public String getDictType() { return dictType; } public void setDictType(String v) { this.dictType = v; }
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
    public String getCreateBy() { return createBy; } public void setCreateBy(String v) { this.createBy = v; }
    public Date getCreateTime() { return createTime; } public void setCreateTime(Date v) { this.createTime = v; }
    public String getUpdateBy() { return updateBy; } public void setUpdateBy(String v) { this.updateBy = v; }
    public Date getUpdateTime() { return updateTime; } public void setUpdateTime(Date v) { this.updateTime = v; }
    public String getRemark() { return remark; } public void setRemark(String v) { this.remark = v; }
}
