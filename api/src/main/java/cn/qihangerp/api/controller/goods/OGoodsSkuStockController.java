package cn.qihangerp.api.controller.goods;

import cn.qihangerp.common.AjaxResult;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.mapper.ErpStockInItemMapper;
import cn.qihangerp.mapper.ErpStockInMapper;
import cn.qihangerp.model.ErpStockIn;
import cn.qihangerp.model.ErpStockInItem;
import cn.qihangerp.model.OGoodsSkuStock;
import cn.qihangerp.model.OGoodsStockLog;
import cn.qihangerp.service.OGoodsSkuStockService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/erp-api/stock")
public class OGoodsSkuStockController {
    @Autowired private OGoodsSkuStockService service;
    @Autowired private ErpStockInMapper stockInMapper;
    @Autowired private ErpStockInItemMapper stockInItemMapper;

    @GetMapping("/list")
    public PageResult<OGoodsSkuStock> list(PageQuery pageQuery, String keyword) {
        return service.list(keyword, pageQuery);
    }

    @GetMapping("/log")
    public PageResult<OGoodsStockLog> logList(PageQuery pageQuery, Long skuId) {
        return service.logList(skuId, pageQuery);
    }

    @PostMapping("/adjust")
    public AjaxResult adjust(@RequestBody Map<String, Object> body) {
        Long skuId = body.get("skuId") != null ? ((Number) body.get("skuId")).longValue() : null;
        Integer changeQuantity = body.get("changeQuantity") != null ? ((Number) body.get("changeQuantity")).intValue() : null;
        String remark = (String) body.get("remark");
        if (skuId == null || changeQuantity == null) return AjaxResult.error("参数不完整");
        try {
            service.adjust(skuId, changeQuantity, remark);
            return AjaxResult.success();
        } catch (RuntimeException e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @GetMapping("/inbound/list")
    public PageResult<ErpStockIn> inboundList(PageQuery pageQuery, String inNum, String sourceNo) {
        var qw = new LambdaQueryWrapper<ErpStockIn>()
            .like(StringUtils.hasText(inNum), ErpStockIn::getInNum, inNum)
            .like(StringUtils.hasText(sourceNo), ErpStockIn::getSourceNo, sourceNo)
            .orderByDesc(ErpStockIn::getId);
        IPage<ErpStockIn> page = stockInMapper.selectPage(pageQuery.build(), qw);
        List<ErpStockIn> records = page.getRecords();
        if (!records.isEmpty()) {
            List<Long> ids = records.stream().map(ErpStockIn::getId).toList();
            List<ErpStockInItem> allItems = stockInItemMapper.selectList(
                new LambdaQueryWrapper<ErpStockInItem>().in(ErpStockInItem::getStockInId, ids));
            var grouped = allItems.stream().collect(java.util.stream.Collectors.groupingBy(ErpStockInItem::getStockInId));
            records.forEach(o -> o.setItemList(grouped.get(o.getId())));
        }
        return PageResult.build(page);
    }

    @GetMapping("/inbound/{id}")
    public AjaxResult inboundGet(@PathVariable Long id) {
        ErpStockIn si = stockInMapper.selectById(id);
        if (si == null) return AjaxResult.error(404, "入库单不存在");
        List<ErpStockInItem> items = stockInItemMapper.selectList(
            new LambdaQueryWrapper<ErpStockInItem>().eq(ErpStockInItem::getStockInId, id));
        si.setItemList(items);
        return AjaxResult.success(si);
    }
}