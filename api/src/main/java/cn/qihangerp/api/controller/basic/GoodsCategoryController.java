package cn.qihangerp.api.controller.basic;

import cn.qihangerp.common.AjaxResult;
import cn.qihangerp.model.GoodsCategory;
import cn.qihangerp.model.GoodsCategoryAttribute;
import cn.qihangerp.model.GoodsCategoryAttributeValue;
import cn.qihangerp.security.common.SecurityUtils;
import cn.qihangerp.service.GoodsCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sys-api/basic/category")
public class GoodsCategoryController {
    @Autowired private GoodsCategoryService service;

    private boolean notAdmin() {
        var u = SecurityUtils.getLoginUser();
        return u == null || u.getRoles().stream().noneMatch(r -> "admin".equals(r.getRoleKey()));
    }

    @GetMapping("/list")
    public AjaxResult list() {
        return AjaxResult.success(service.list());
    }

    @PostMapping("/save")
    public AjaxResult save(@RequestBody GoodsCategory category) {
        if (notAdmin()) return AjaxResult.error(403, "权限不足");
        service.save(category);
        return AjaxResult.success();
    }

    @DeleteMapping("/{id}")
    public AjaxResult delete(@PathVariable Long id) {
        if (notAdmin()) return AjaxResult.error(403, "权限不足");
        service.delete(id);
        return AjaxResult.success();
    }

    // ─── 分类规格属性 ───

    @GetMapping("/attribute/list")
    public AjaxResult attributeList(Long categoryId) {
        return AjaxResult.success(service.listAttributes(categoryId));
    }

    @PostMapping("/attribute/save")
    public AjaxResult attributeSave(@RequestBody GoodsCategoryAttribute attr) {
        if (notAdmin()) return AjaxResult.error(403, "权限不足");
        service.saveAttribute(attr);
        return AjaxResult.success();
    }

    @DeleteMapping("/attribute/{id}")
    public AjaxResult attributeDelete(@PathVariable Long id) {
        if (notAdmin()) return AjaxResult.error(403, "权限不足");
        service.deleteAttribute(id);
        return AjaxResult.success();
    }

    // ─── 规格属性值 ───

    @GetMapping("/attribute/value/list")
    public AjaxResult attrValueList(Long categoryAttributeId) {
        return AjaxResult.success(service.listAttrValues(categoryAttributeId));
    }

    @PostMapping("/attribute/value/save")
    public AjaxResult attrValueSave(@RequestBody GoodsCategoryAttributeValue val) {
        if (notAdmin()) return AjaxResult.error(403, "权限不足");
        service.saveAttrValue(val);
        return AjaxResult.success();
    }

    @DeleteMapping("/attribute/value/{id}")
    public AjaxResult attrValueDelete(@PathVariable Long id) {
        if (notAdmin()) return AjaxResult.error(403, "权限不足");
        service.deleteAttrValue(id);
        return AjaxResult.success();
    }
}