package cn.qihangerp.model;
import com.baomidou.mybatisplus.annotation.TableId;
public class SysConfig {
    @TableId private Long configId; private String configName; private String configKey;
    private String configValue; private String configType; private String remark;
    public Long getConfigId() { return configId; } public void setConfigId(Long v) { this.configId = v; }
    public String getConfigName() { return configName; } public void setConfigName(String v) { this.configName = v; }
    public String getConfigKey() { return configKey; } public void setConfigKey(String v) { this.configKey = v; }
    public String getConfigValue() { return configValue; } public void setConfigValue(String v) { this.configValue = v; }
    public String getConfigType() { return configType; } public void setConfigType(String v) { this.configType = v; }
    public String getRemark() { return remark; } public void setRemark(String v) { this.remark = v; }
}