package cn.qihang.ai.api.mapper;

import cn.qihang.ai.business.model.SysDictData;
import cn.qihang.ai.business.model.SysDictType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysDictMapper {
    interface TypeMapper extends BaseMapper<SysDictType> {
        @Select("SELECT * FROM sys_dict_type WHERE status = '0' ORDER BY dict_id")
        List<SysDictType> selectAllActive();
    }
    interface DataMapper extends BaseMapper<SysDictData> {
        @Select("SELECT * FROM sys_dict_data WHERE dict_type = #{dictType} AND status = '0' ORDER BY dict_sort")
        List<SysDictData> selectByType(String dictType);
    }
}
