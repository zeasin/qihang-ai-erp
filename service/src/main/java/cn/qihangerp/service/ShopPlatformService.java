package cn.qihangerp.service;
import cn.qihangerp.mapper.ShopPlatformMapper;
import cn.qihangerp.model.OShopPlatform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class ShopPlatformService {
    @Autowired private ShopPlatformMapper mapper;
    public List<OShopPlatform> list() { return mapper.selectList(null); }
    public void save(OShopPlatform p) { if (p.getId() == null) mapper.insert(p); else mapper.updateById(p); }
    public void delete(Integer id) { mapper.deleteById(id); }
}