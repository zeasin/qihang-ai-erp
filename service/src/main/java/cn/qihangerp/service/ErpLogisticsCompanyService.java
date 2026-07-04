package cn.qihangerp.service;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.mapper.ErpLogisticsCompanyMapper;
import cn.qihangerp.model.ErpLogisticsCompany;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ErpLogisticsCompanyService {
    @Autowired private ErpLogisticsCompanyMapper mapper;

    public PageResult<ErpLogisticsCompany> list(String name, Integer status, PageQuery pageQuery) {
        var qw = new LambdaQueryWrapper<ErpLogisticsCompany>()
            .like(StringUtils.hasText(name), ErpLogisticsCompany::getName, name)
            .eq(status != null, ErpLogisticsCompany::getStatus, status)
            .orderByDesc(ErpLogisticsCompany::getId);
        IPage<ErpLogisticsCompany> page = mapper.selectPage(pageQuery.build(), qw);
        return PageResult.build(page);
    }

    public void save(ErpLogisticsCompany l) {
        if (l.getId() == null) mapper.insert(l);
        else mapper.updateById(l);
    }

    public void delete(Long id) { mapper.deleteById(id); }
}