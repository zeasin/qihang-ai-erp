package cn.qihang.ai.business.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

@TableName("sys_user")
public class SysUser {
    @TableId(type = IdType.AUTO)
    private Long userId; private Long deptId; private Long merchantId;
    private String userName; private String nickName; private String userType;
    private String email; private String phonenumber; private String sex;
    private String avatar; private String password; private String status;
    private String delFlag; private String loginIp; private Date loginDate;
    private String createBy; private Date createTime; private String updateBy;
    private Date updateTime; private String remark;

    public Long getUserId() { return userId; } public void setUserId(Long v) { this.userId = v; }
    public Long getDeptId() { return deptId; } public void setDeptId(Long v) { this.deptId = v; }
    public Long getMerchantId() { return merchantId; } public void setMerchantId(Long v) { this.merchantId = v; }
    public String getUserName() { return userName; } public void setUserName(String v) { this.userName = v; }
    public String getNickName() { return nickName; } public void setNickName(String v) { this.nickName = v; }
    public String getUserType() { return userType; } public void setUserType(String v) { this.userType = v; }
    public String getEmail() { return email; } public void setEmail(String v) { this.email = v; }
    public String getPhonenumber() { return phonenumber; } public void setPhonenumber(String v) { this.phonenumber = v; }
    public String getSex() { return sex; } public void setSex(String v) { this.sex = v; }
    public String getAvatar() { return avatar; } public void setAvatar(String v) { this.avatar = v; }
    public String getPassword() { return password; } public void setPassword(String v) { this.password = v; }
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
    public String getDelFlag() { return delFlag; } public void setDelFlag(String v) { this.delFlag = v; }
    public String getLoginIp() { return loginIp; } public void setLoginIp(String v) { this.loginIp = v; }
    public Date getLoginDate() { return loginDate; } public void setLoginDate(Date v) { this.loginDate = v; }
    public String getCreateBy() { return createBy; } public void setCreateBy(String v) { this.createBy = v; }
    public Date getCreateTime() { return createTime; } public void setCreateTime(Date v) { this.createTime = v; }
    public String getUpdateBy() { return updateBy; } public void setUpdateBy(String v) { this.updateBy = v; }
    public Date getUpdateTime() { return updateTime; } public void setUpdateTime(Date v) { this.updateTime = v; }
    public String getRemark() { return remark; } public void setRemark(String v) { this.remark = v; }
}
