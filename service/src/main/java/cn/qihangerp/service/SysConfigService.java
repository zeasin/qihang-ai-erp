package cn.qihangerp.service;
import cn.qihangerp.mapper.SysConfigMapper;
import cn.qihangerp.model.SysConfig;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class SysConfigService {
    @Autowired private SysConfigMapper configMapper;
    public List<SysConfig> list() { return configMapper.selectList(null); }
    public void save(SysConfig config) { if (config.getConfigId() == null) configMapper.insert(config); else configMapper.updateById(config); }
    public void delete(Long id) { configMapper.deleteById(id); }
    public String getValue(String key) {
        var list = configMapper.selectList(new LambdaQueryWrapper<SysConfig>().eq(SysConfig::getConfigKey, key));
        return list.isEmpty() ? null : list.get(0).getConfigValue();
    }
}