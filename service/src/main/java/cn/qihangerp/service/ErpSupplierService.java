package cn.qihangerp.service;
import cn.qihangerp.mapper.ErpSupplierMapper;
import cn.qihangerp.model.ErpSupplier;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ErpSupplierService {
    @Autowired private ErpSupplierMapper mapper;

    public List<ErpSupplier> list(ErpSupplier bo) {
        var qw = new LambdaQueryWrapper<ErpSupplier>()
            .eq(ErpSupplier::getIsDelete, 0)
            .eq(bo.getIsShipper() != null, ErpSupplier::getIsShipper, bo.getIsShipper());
        if (bo.getName() != null && !bo.getName().isEmpty()) qw.like(ErpSupplier::getName, bo.getName());
        if (bo.getNumber() != null && !bo.getNumber().isEmpty()) qw.like(ErpSupplier::getNumber, bo.getNumber());
        if (bo.getLinkMan() != null && !bo.getLinkMan().isEmpty()) qw.like(ErpSupplier::getLinkMan, bo.getLinkMan());
        return mapper.selectList(qw);
    }

    public void save(ErpSupplier s) {
        if (s.getId() == null) mapper.insert(s);
        else mapper.updateById(s);
    }

    public void delete(Long id) { mapper.deleteById(id); }
}