package cn.qihangerp.service;
import cn.qihangerp.mapper.ErpWarehouseMapper;
import cn.qihangerp.model.ErpWarehouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ErpWarehouseService {
    @Autowired private ErpWarehouseMapper mapper;

    public List<ErpWarehouse> list() { return mapper.selectList(null); }

    public void save(ErpWarehouse w) {
        if (w.getId() == null) mapper.insert(w);
        else mapper.updateById(w);
    }

    public void delete(Long id) { mapper.deleteById(id); }
}