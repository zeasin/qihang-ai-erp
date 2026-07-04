package cn.qihangerp.service;
import cn.qihangerp.mapper.ErpLogisticsCompanyMapper;
import cn.qihangerp.model.ErpLogisticsCompany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ErpLogisticsCompanyService {
    @Autowired private ErpLogisticsCompanyMapper mapper;

    public List<ErpLogisticsCompany> list() { return mapper.selectList(null); }

    public void save(ErpLogisticsCompany l) {
        if (l.getId() == null) mapper.insert(l);
        else mapper.updateById(l);
    }

    public void delete(Long id) { mapper.deleteById(id); }
}