package cn.qihang.ai.api.controller.sys;

import cn.qihang.ai.api.security.LoginUser;
import cn.qihang.ai.api.security.SysDictService;
import cn.qihang.ai.api.security.common.SecurityUtils;
import cn.qihang.ai.business.model.SysDictData;
import cn.qihang.ai.business.model.SysDictType;
import cn.qihang.ai.common.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sys-api/system/dict")
public class SysDictController {

    @Autowired
    private SysDictService dictService;

    private boolean isNotAdmin() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null) return true;
        return loginUser.getRoles().stream().noneMatch(r -> "admin".equals(r.getRoleKey()));
    }

    @GetMapping("/type/list")
    public AjaxResult listTypes() { if (isNotAdmin()) return AjaxResult.error(403, "权限不足，仅管理员可操作"); return AjaxResult.success(dictService.listTypes()); }

    @PostMapping("/type/save")
    public AjaxResult saveType(@RequestBody SysDictType type) { if (isNotAdmin()) return AjaxResult.error(403, "权限不足，仅管理员可操作"); dictService.saveType(type); return AjaxResult.success(); }

    @DeleteMapping("/type/{id}")
    public AjaxResult deleteType(@PathVariable Long id) { if (isNotAdmin()) return AjaxResult.error(403, "权限不足，仅管理员可操作"); dictService.deleteType(id); return AjaxResult.success(); }

    @GetMapping("/data/list")
    public AjaxResult listData(@RequestParam(required = false) String dictType) { if (isNotAdmin()) return AjaxResult.error(403, "权限不足，仅管理员可操作"); return AjaxResult.success(dictService.listData(dictType)); }

    @PostMapping("/data/save")
    public AjaxResult saveData(@RequestBody SysDictData data) { if (isNotAdmin()) return AjaxResult.error(403, "权限不足，仅管理员可操作"); dictService.saveData(data); return AjaxResult.success(); }

    @DeleteMapping("/data/{id}")
    public AjaxResult deleteData(@PathVariable Long id) { if (isNotAdmin()) return AjaxResult.error(403, "权限不足，仅管理员可操作"); dictService.deleteData(id); return AjaxResult.success(); }
}
