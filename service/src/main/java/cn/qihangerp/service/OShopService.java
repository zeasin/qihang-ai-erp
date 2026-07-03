package cn.qihangerp.service;
import cn.qihangerp.mapper.OShopMapper;
import cn.qihangerp.model.OShop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class OShopService {
    @Autowired private OShopMapper mapper;
    public List<OShop> list() { return mapper.selectList(null); }
    public void save(OShop shop) { if (shop.getId() == null) mapper.insert(shop); else mapper.updateById(shop); }
    public void delete(Long id) { mapper.deleteById(id); }
}