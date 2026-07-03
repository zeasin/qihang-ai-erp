package cn.qihangerp.service;
import cn.qihangerp.mapper.GoodsBrandMapper;
import cn.qihangerp.model.GoodsBrand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GoodsBrandService {
    @Autowired private GoodsBrandMapper mapper;

    public List<GoodsBrand> list() { return mapper.selectList(null); }

    public void save(GoodsBrand brand) {
        if (brand.getId() == null) mapper.insert(brand);
        else mapper.updateById(brand);
    }

    public void delete(Long id) { mapper.deleteById(id); }
}