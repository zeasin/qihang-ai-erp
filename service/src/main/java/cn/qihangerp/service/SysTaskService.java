package cn.qihangerp.service;
import cn.qihangerp.mapper.SysTaskMapper;
import cn.qihangerp.model.SysTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class SysTaskService {
    @Autowired private SysTaskMapper taskMapper;
    public List<SysTask> list() { return taskMapper.selectList(null); }
    public SysTask getById(Long id) { return taskMapper.selectById(id); }
    public void update(SysTask task) { taskMapper.updateById(task); }
}