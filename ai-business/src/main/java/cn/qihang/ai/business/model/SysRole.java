package cn.qihang.ai.business.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

@TableName("sys_role")
public class SysRole {
    @TableId(type = IdType.AUTO)
    private Long roleId; private String roleName; private String roleKey;
    private String status; private String delFlag; private String createBy;
    private Date createTime; private String updateBy; private Date updateTime; private String remark;

    public Long getRoleId() { return roleId; } public void setRoleId(Long v) { this.roleId = v; }
    public String getRoleName() { return roleName; } public void setRoleName(String v) { this.roleName = v; }
    public String getRoleKey() { return roleKey; } public void setRoleKey(String v) { this.roleKey = v; }
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
    public String getDelFlag() { return delFlag; } public void setDelFlag(String v) { this.delFlag = v; }
    public String getCreateBy() { return createBy; } public void setCreateBy(String v) { this.createBy = v; }
    public Date getCreateTime() { return createTime; } public void setCreateTime(Date v) { this.createTime = v; }
    public String getUpdateBy() { return updateBy; } public void setUpdateBy(String v) { this.updateBy = v; }
    public Date getUpdateTime() { return updateTime; } public void setUpdateTime(Date v) { this.updateTime = v; }
    public String getRemark() { return remark; } public void setRemark(String v) { this.remark = v; }
}
