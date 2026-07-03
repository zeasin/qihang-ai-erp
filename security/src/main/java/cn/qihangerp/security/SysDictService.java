package cn.qihangerp.security;

import cn.qihangerp.mapper.SysDictMapper.TypeMapper;
import cn.qihangerp.mapper.SysDictMapper.DataMapper;
import cn.qihangerp.model.SysDictData;
import cn.qihangerp.model.SysDictType;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SysDictService {

    @Autowired
    private TypeMapper typeMapper;
    @Autowired
    private DataMapper dataMapper;

    // ─── 字典类型 ───
    public List<SysDictType> listTypes() { return typeMapper.selectList(null); }

    public void saveType(SysDictType type) {
        if (type.getDictId() == null) { type.setCreateTime(new Date()); typeMapper.insert(type); }
        else { typeMapper.updateById(type); }
    }

    public void deleteType(Long dictId) {
        SysDictType type = typeMapper.selectById(dictId);
        if (type != null) {
            dataMapper.delete(new LambdaQueryWrapper<SysDictData>().eq(SysDictData::getDictType, type.getDictType()));
            typeMapper.deleteById(dictId);
        }
    }

    public List<SysDictData> listData(String dictType) {
        if (dictType != null && !dictType.isEmpty())
            return dataMapper.selectList(new LambdaQueryWrapper<SysDictData>().eq(SysDictData::getDictType, dictType).orderByAsc(SysDictData::getDictSort));
        return dataMapper.selectList(null);
    }

    public void saveData(SysDictData data) {
        if (data.getDictCode() == null) { data.setCreateTime(new Date()); dataMapper.insert(data); }
        else { dataMapper.updateById(data); }
    }

    public void deleteData(Long dictCode) { dataMapper.deleteById(dictCode); }
}
