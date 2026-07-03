package cn.qihangerp.service;
import cn.qihangerp.mapper.GoodsCategoryMapper;
import cn.qihangerp.model.GoodsCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GoodsCategoryService {
    @Autowired private GoodsCategoryMapper mapper;

    public List<GoodsCategory> list() { return mapper.selectList(null); }

    public void save(GoodsCategory category) {
        if (category.getId() == null) mapper.insert(category);
        else mapper.updateById(category);
    }

    public void delete(Long id) { mapper.deleteById(id); }
}