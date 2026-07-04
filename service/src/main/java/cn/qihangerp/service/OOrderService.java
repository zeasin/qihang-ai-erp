package cn.qihangerp.service;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.mapper.OOrderItemMapper;
import cn.qihangerp.mapper.OOrderMapper;
import cn.qihangerp.model.OOrder;
import cn.qihangerp.model.OOrderItem;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OOrderService {
    @Autowired private OOrderMapper mapper;
    @Autowired private OOrderItemMapper itemMapper;

    public PageResult<OOrder> list(String orderNum, String receiverName, Integer orderStatus,
                                   String startTime, String endTime, PageQuery pageQuery) {
        var qw = new LambdaQueryWrapper<OOrder>()
            .like(StringUtils.hasText(orderNum), OOrder::getOrderNum, orderNum)
            .like(StringUtils.hasText(receiverName), OOrder::getReceiverName, receiverName)
            .eq(orderStatus != null, OOrder::getOrderStatus, orderStatus)
            .apply(StringUtils.hasText(startTime), "order_time >= TO_TIMESTAMP({0}, 'YYYY-MM-DD HH24:MI:SS')", startTime)
            .apply(StringUtils.hasText(endTime), "order_time <= TO_TIMESTAMP({0}, 'YYYY-MM-DD HH24:MI:SS')", endTime)
            .orderByDesc(OOrder::getId);
        IPage<OOrder> page = mapper.selectPage(pageQuery.build(), qw);
        List<OOrder> records = page.getRecords();
        if (!records.isEmpty()) {
            List<Long> ids = records.stream().map(OOrder::getId).toList();
            List<OOrderItem> allItems = itemMapper.selectList(
                new LambdaQueryWrapper<OOrderItem>().in(OOrderItem::getOrderId, ids));
            Map<Long, List<OOrderItem>> grouped = allItems.stream()
                .collect(Collectors.groupingBy(OOrderItem::getOrderId));
            records.forEach(o -> o.setItemList(grouped.get(o.getId())));
        }
        return PageResult.build(page);
    }

    public OOrder getById(Long id) {
        OOrder order = mapper.selectById(id);
        if (order != null) {
            List<OOrderItem> items = itemMapper.selectList(
                new LambdaQueryWrapper<OOrderItem>().eq(OOrderItem::getOrderId, id));
            order.setItemList(items);
        }
        return order;
    }

    @Transactional
    public void save(OOrder order, List<OOrderItem> items) {
        if (order.getId() == null) {
            mapper.insert(order);
        } else {
            mapper.updateById(order);
            itemMapper.delete(new LambdaQueryWrapper<OOrderItem>().eq(OOrderItem::getOrderId, order.getId()));
        }
        if (items != null) {
            for (OOrderItem item : items) {
                item.setOrderId(order.getId());
                item.setOrderNum(order.getOrderNum());
                itemMapper.insert(item);
            }
        }
    }

    public void pay(Long id) {
        OOrder order = mapper.selectById(id);
        if (order == null) throw new RuntimeException("订单不存在");
        if (order.getOrderStatus() != 0) throw new RuntimeException("当前状态不能付款");
        order.setOrderStatus(1);
        order.setUpdateTime(new Date());
        mapper.updateById(order);
    }

    public void delete(Long id) {
        itemMapper.delete(new LambdaQueryWrapper<OOrderItem>().eq(OOrderItem::getOrderId, id));
        mapper.deleteById(id);
    }
}