package cn.qihangerp.service;
import cn.qihangerp.mapper.ErpMerchantMapper;
import cn.qihangerp.model.ErpMerchant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class ErpMerchantService {
    @Autowired private ErpMerchantMapper mapper;
    public List<ErpMerchant> list() { return mapper.selectList(null); }
    public void save(ErpMerchant m) { if (m.getId() == null) mapper.insert(m); else mapper.updateById(m); }
    public void delete(Long id) { mapper.deleteById(id); }
}