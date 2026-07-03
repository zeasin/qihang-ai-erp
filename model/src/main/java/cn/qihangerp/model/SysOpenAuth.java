package cn.qihangerp.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

/** 开放接口授权表 sys_open_auth */
@TableName("sys_open_auth")
public class SysOpenAuth {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Long id;
    private String appKey;
    private String appSecret;
    private String whiteList;
    private Integer status;
    private String remark;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
    private Integer type;

    public Long getId() {
        return id;
    }

    public void setId(Long v) {
        this.id = v;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String v) {
        this.appKey = v;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String v) {
        this.appSecret = v;
    }

    public String getWhiteList() {
        return whiteList;
    }

    public void setWhiteList(String v) {
        this.whiteList = v;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer v) {
        this.status = v;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String v) {
        this.remark = v;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String v) {
        this.createBy = v;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date v) {
        this.createTime = v;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String v) {
        this.updateBy = v;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date v) {
        this.updateTime = v;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer v) {
        this.type = v;
    }
}
