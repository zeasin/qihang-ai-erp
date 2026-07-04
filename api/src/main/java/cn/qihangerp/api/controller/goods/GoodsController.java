package cn.qihangerp.api.controller.goods;

import cn.qihangerp.common.AjaxResult;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.model.OGoods;
import cn.qihangerp.model.OGoodsSku;
import cn.qihangerp.security.common.SecurityUtils;
import cn.qihangerp.service.OGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/erp-api/goods")
public class GoodsController {
    @Autowired private OGoodsService service;

    private boolean notAdmin() {
        var u = SecurityUtils.getLoginUser();
        return u == null || u.getRoles().stream().noneMatch(r -> "admin".equals(r.getRoleKey()));
    }

    @GetMapping("/list")
    public PageResult<OGoods> list(PageQuery pageQuery, String name, Long categoryId, Long brandId, Integer status) {
        return service.list(name, categoryId, brandId, status, pageQuery);
    }

    @GetMapping("/{id}")
    public AjaxResult getById(@PathVariable Long id) {
        return AjaxResult.success(service.getById(id));
    }

    @PostMapping("/save")
    public AjaxResult save(@RequestBody OGoods goods) {
        if (notAdmin()) return AjaxResult.error(403, "权限不足");
        service.save(goods);
        return AjaxResult.success();
    }

    @PostMapping("/add")
    public AjaxResult add(@RequestBody Map<String, Object> body) {
        if (notAdmin()) return AjaxResult.error(403, "权限不足");
        OGoods goods = new OGoods();
        goods.setName((String) body.get("name"));
        goods.setImage((String) body.get("image"));
        goods.setGoodsNum((String) body.get("goodsNum"));
        goods.setUnitName((String) body.get("unitName"));
        goods.setCategoryId(body.get("categoryId") != null ? ((Number) body.get("categoryId")).longValue() : null);
        goods.setBrandId(body.get("brandId") != null ? ((Number) body.get("brandId")).longValue() : null);
        goods.setBarCode((String) body.get("barCode"));
        goods.setRemark((String) body.get("remark"));
        goods.setRetailPrice(body.get("retailPrice") != null ? new java.math.BigDecimal(body.get("retailPrice").toString()) : null);
        goods.setWeight(body.get("weight") != null ? new java.math.BigDecimal(body.get("weight").toString()) : null);
        goods.setStatus(1);

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> skuMaps = (List<Map<String, Object>>) body.get("skuList");
        List<OGoodsSku> skuList = null;
        if (skuMaps != null) {
            skuList = skuMaps.stream().map(s -> {
                OGoodsSku sku = new OGoodsSku();
                sku.setSkuCode((String) s.get("skuCode"));
                sku.setSkuName((String) s.get("skuName"));
                sku.setColorValue((String) s.get("colorValue"));
                sku.setSizeValue((String) s.get("sizeValue"));
                sku.setStyleValue((String) s.get("styleValue"));
                sku.setBarCode((String) s.get("barCode"));
                sku.setRetailPrice(s.get("retailPrice") != null ? new java.math.BigDecimal(s.get("retailPrice").toString()) : null);
                sku.setUnitCost(s.get("unitCost") != null ? new java.math.BigDecimal(s.get("unitCost").toString()) : null);
                sku.setRemark((String) s.get("remark"));
                sku.setStatus(1);
                return sku;
            }).toList();
        }

        return service.addWithSkus(goods, skuList);
    }

    @DeleteMapping("/{id}")
    public AjaxResult delete(@PathVariable Long id) {
        if (notAdmin()) return AjaxResult.error(403, "权限不足");
        service.delete(id);
        return AjaxResult.success();
    }
}
