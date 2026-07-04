package cn.qihangerp.mapper;

import cn.qihangerp.model.OGoodsSku;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OGoodsSkuMapper extends BaseMapper<OGoodsSku> {
    IPage<OGoodsSku> selectSkuPageList(IPage<OGoodsSku> page, @Param("keyword") String keyword);
}
