package cn.qihangerp.service;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.mapper.OGoodsMapper;
import cn.qihangerp.model.OGoods;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class OGoodsService {
    @Autowired private OGoodsMapper mapper;

    public PageResult<OGoods> list(String name, Long categoryId, Long brandId, Integer status, PageQuery pageQuery) {
        var qw = new LambdaQueryWrapper<OGoods>()
            .like(StringUtils.hasText(name), OGoods::getName, name)
            .eq(categoryId != null && categoryId > 0, OGoods::getCategoryId, categoryId)
            .eq(brandId != null && brandId > 0, OGoods::getBrandId, brandId)
            .eq(status != null, OGoods::getStatus, status)
            .orderByDesc(OGoods::getId);
        IPage<OGoods> page = mapper.selectPage(pageQuery.build(), qw);
        return PageResult.build(page);
    }

    public OGoods getById(Long id) {
        return mapper.selectById(id);
    }

    public void save(OGoods goods) {
        if (goods.getId() == null) {
            mapper.insert(goods);
        } else {
            mapper.updateById(goods);
        }
    }

    public void delete(Long id) {
        mapper.deleteById(id);
    }
}
