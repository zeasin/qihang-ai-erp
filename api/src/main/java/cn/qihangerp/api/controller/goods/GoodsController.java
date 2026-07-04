package cn.qihangerp.api.controller.goods;

import cn.qihangerp.common.AjaxResult;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.model.OGoods;
import cn.qihangerp.security.common.SecurityUtils;
import cn.qihangerp.service.OGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sys-api/goods")
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

    @DeleteMapping("/{id}")
    public AjaxResult delete(@PathVariable Long id) {
        if (notAdmin()) return AjaxResult.error(403, "权限不足");
        service.delete(id);
        return AjaxResult.success();
    }
}
