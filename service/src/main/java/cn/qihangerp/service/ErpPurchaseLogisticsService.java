package cn.qihangerp.service;
import cn.qihangerp.mapper.ErpPurchaseLogisticsMapper;
import cn.qihangerp.model.ErpPurchaseLogistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ErpPurchaseLogisticsService {
    @Autowired private ErpPurchaseLogisticsMapper mapper;

    public List<ErpPurchaseLogistics> list() { return mapper.selectList(null); }

    public void save(ErpPurchaseLogistics l) {
        if (l.getId() == null) mapper.insert(l);
        else mapper.updateById(l);
    }

    public void delete(Long id) { mapper.deleteById(id); }
}