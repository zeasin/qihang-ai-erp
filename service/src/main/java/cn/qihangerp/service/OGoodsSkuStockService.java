package cn.qihangerp.service;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.mapper.OGoodsSkuStockMapper;
import cn.qihangerp.mapper.OGoodsStockLogMapper;
import cn.qihangerp.model.OGoodsSkuStock;
import cn.qihangerp.model.OGoodsStockLog;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;

@Service
public class OGoodsSkuStockService {
    @Autowired private OGoodsSkuStockMapper mapper;
    @Autowired private OGoodsStockLogMapper logMapper;

    public PageResult<OGoodsSkuStock> list(String keyword, PageQuery pageQuery) {
        var qw = new LambdaQueryWrapper<OGoodsSkuStock>()
            .and(StringUtils.hasText(keyword), w -> w
                .like(OGoodsSkuStock::getGoodsName, keyword)
                .or().like(OGoodsSkuStock::getSkuCode, keyword)
                .or().like(OGoodsSkuStock::getSkuName, keyword))
            .orderByDesc(OGoodsSkuStock::getId);
        IPage<OGoodsSkuStock> page = mapper.selectPage(pageQuery.build(), qw);
        return PageResult.build(page);
    }

    public OGoodsSkuStock getBySkuId(Long skuId) {
        return mapper.selectOne(new LambdaQueryWrapper<OGoodsSkuStock>().eq(OGoodsSkuStock::getSkuId, skuId));
    }

    @Transactional
    public void adjust(Long skuId, int changeQuantity, String remark) {
        OGoodsSkuStock stock = mapper.selectOne(
            new LambdaQueryWrapper<OGoodsSkuStock>().eq(OGoodsSkuStock::getSkuId, skuId));
        if (stock == null) throw new RuntimeException("SKU库存记录不存在");

        int before = stock.getQuantity() != null ? stock.getQuantity() : 0;
        int after = before + changeQuantity;
        if (after < 0) throw new RuntimeException("库存不足，无法调整");

        stock.setQuantity(after);
        stock.setUpdateTime(new Date());
        mapper.updateById(stock);

        OGoodsStockLog log = new OGoodsStockLog();
        log.setSkuId(skuId);
        log.setGoodsId(stock.getGoodsId());
        log.setSkuCode(stock.getSkuCode());
        log.setGoodsName(stock.getGoodsName());
        log.setSkuName(stock.getSkuName());
        log.setBeforeQuantity(before);
        log.setChangeQuantity(changeQuantity);
        log.setAfterQuantity(after);
        log.setType(3);
        log.setRemark(remark);
        log.setCreateTime(new Date());
        logMapper.insert(log);
    }

    public PageResult<OGoodsStockLog> logList(Long skuId, PageQuery pageQuery) {
        var qw = new LambdaQueryWrapper<OGoodsStockLog>()
            .eq(skuId != null, OGoodsStockLog::getSkuId, skuId)
            .orderByDesc(OGoodsStockLog::getId);
        IPage<OGoodsStockLog> page = logMapper.selectPage(pageQuery.build(), qw);
        return PageResult.build(page);
    }
}