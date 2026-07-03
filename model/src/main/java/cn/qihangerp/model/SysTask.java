package cn.qihangerp.model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
@TableName("sys_task")
public class SysTask {
    @TableId private Long id; private String taskName; private String cron;
    private String method; private String remark; private Integer status;
    public Long getId() { return id; } public void setId(Long v) { this.id = v; }
    public String getTaskName() { return taskName; } public void setTaskName(String v) { this.taskName = v; }
    public String getCron() { return cron; } public void setCron(String v) { this.cron = v; }
    public String getMethod() { return method; } public void setMethod(String v) { this.method = v; }
    public String getRemark() { return remark; } public void setRemark(String v) { this.remark = v; }
    public Integer getStatus() { return status; } public void setStatus(Integer v) { this.status = v; }
}