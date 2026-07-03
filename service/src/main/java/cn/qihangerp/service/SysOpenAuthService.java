package cn.qihangerp.service;
import cn.qihangerp.mapper.SysOpenAuthMapper;
import cn.qihangerp.model.SysOpenAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
@Service
public class SysOpenAuthService {
    @Autowired private SysOpenAuthMapper openAuthMapper;
    public List<SysOpenAuth> list() { return openAuthMapper.selectList(null); }
    public void save(SysOpenAuth auth) {
        if (auth.getId() == null) { auth.setCreateTime(new Date()); openAuthMapper.insert(auth); }
        else openAuthMapper.updateById(auth);
    }
    public void delete(Long id) { openAuthMapper.deleteById(id); }
}