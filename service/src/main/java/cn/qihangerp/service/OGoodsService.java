package cn.qihangerp.service;

import cn.qihangerp.common.AjaxResult;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.mapper.OGoodsMapper;
import cn.qihangerp.mapper.OGoodsSkuMapper;
import cn.qihangerp.model.OGoods;
import cn.qihangerp.model.OGoodsSku;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class OGoodsService {
    @Autowired private OGoodsMapper mapper;
    @Autowired private OGoodsSkuMapper skuMapper;

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

    @Transactional
    public AjaxResult addWithSkus(OGoods goods, List<OGoodsSku> skuList) {
        if (!StringUtils.hasText(goods.getName())) {
            return AjaxResult.error("商品名称不能为空");
        }
        mapper.insert(goods);
        if (skuList != null && !skuList.isEmpty()) {
            for (OGoodsSku sku : skuList) {
                sku.setGoodsId(goods.getId());
                sku.setGoodsName(goods.getName());
                sku.setGoodsNum(goods.getGoodsNum());
                sku.setMerchantId(goods.getMerchantId());
                skuMapper.insert(sku);
            }
        }
        return AjaxResult.success();
    }

    public PageResult<OGoodsSku> skuList(String keyword, PageQuery pageQuery) {
        IPage<OGoodsSku> page = skuMapper.selectSkuPageList(pageQuery.build(), keyword);
        return PageResult.build(page);
    }

    public void delete(Long id) {
        mapper.deleteById(id);
    }
}
