package cn.qihangerp.service;
import cn.qihangerp.mapper.GoodsCategoryAttributeMapper;
import cn.qihangerp.mapper.GoodsCategoryAttributeValueMapper;
import cn.qihangerp.mapper.GoodsCategoryMapper;
import cn.qihangerp.model.GoodsCategory;
import cn.qihangerp.model.GoodsCategoryAttribute;
import cn.qihangerp.model.GoodsCategoryAttributeValue;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GoodsCategoryService {
    @Autowired private GoodsCategoryMapper mapper;
    @Autowired private GoodsCategoryAttributeMapper attrMapper;
    @Autowired private GoodsCategoryAttributeValueMapper attrValMapper;

    public List<GoodsCategory> list() { return mapper.selectList(null); }

    public void save(GoodsCategory category) {
        if (category.getId() == null) mapper.insert(category);
        else mapper.updateById(category);
    }

    public void delete(Long id) { mapper.deleteById(id); }

    // ─── 分类规格属性 ───

    public List<GoodsCategoryAttribute> listAttributes(Long categoryId) {
        return attrMapper.selectList(
            new LambdaQueryWrapper<GoodsCategoryAttribute>().eq(GoodsCategoryAttribute::getCategoryId, categoryId)
        );
    }

    public void saveAttribute(GoodsCategoryAttribute attr) {
        if (attr.getId() == null) attrMapper.insert(attr);
        else attrMapper.updateById(attr);
    }

    public void deleteAttribute(Long id) { attrMapper.deleteById(id); }

    // ─── 规格属性值 ───

    public List<GoodsCategoryAttributeValue> listAttrValues(Long categoryAttributeId) {
        return attrValMapper.selectList(
            new LambdaQueryWrapper<GoodsCategoryAttributeValue>().eq(GoodsCategoryAttributeValue::getCategoryAttributeId, categoryAttributeId)
        );
    }

    public void saveAttrValue(GoodsCategoryAttributeValue val) {
        if (val.getId() == null) attrValMapper.insert(val);
        else attrValMapper.updateById(val);
    }

    public void deleteAttrValue(Long id) { attrValMapper.deleteById(id); }
}