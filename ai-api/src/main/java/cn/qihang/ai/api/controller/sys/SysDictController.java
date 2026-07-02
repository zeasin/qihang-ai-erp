package cn.qihang.ai.api.controller.sys;

import cn.qihang.ai.api.security.SysDictService;
import cn.qihang.ai.business.model.SysDictData;
import cn.qihang.ai.business.model.SysDictType;
import cn.qihang.ai.common.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sys-api/system/dict")
public class SysDictController {

    @Autowired
    private SysDictService dictService;

    @GetMapping("/type/list")
    public AjaxResult listTypes() { return AjaxResult.success(dictService.listTypes()); }

    @PostMapping("/type/save")
    public AjaxResult saveType(@RequestBody SysDictType type) { dictService.saveType(type); return AjaxResult.success(); }

    @DeleteMapping("/type/{id}")
    public AjaxResult deleteType(@PathVariable Long id) { dictService.deleteType(id); return AjaxResult.success(); }

    @GetMapping("/data/list")
    public AjaxResult listData(@RequestParam(required = false) String dictType) { return AjaxResult.success(dictService.listData(dictType)); }

    @PostMapping("/data/save")
    public AjaxResult saveData(@RequestBody SysDictData data) { dictService.saveData(data); return AjaxResult.success(); }

    @DeleteMapping("/data/{id}")
    public AjaxResult deleteData(@PathVariable Long id) { dictService.deleteData(id); return AjaxResult.success(); }
}
