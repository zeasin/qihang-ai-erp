package cn.qihangerp.service;
import cn.qihangerp.mapper.ShopRegionMapper;
import cn.qihangerp.model.ShopRegion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ShopRegionService {
    @Autowired private ShopRegionMapper mapper;

    public List<ShopRegion> list() { return mapper.selectList(null); }

    public void save(ShopRegion r) {
        if (r.getId() == null) mapper.insert(r);
        else mapper.updateById(r);
    }

    public void delete(Long id) { mapper.deleteById(id); }
}