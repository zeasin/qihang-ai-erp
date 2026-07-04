package cn.qihangerp.common;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageQuery {
    private int pageNum = 1;
    private int pageSize = 20;
    private String orderBy;
    private boolean isAsc = true;

    public <T> Page<T> build() {
        int pageIndex = Math.max(pageNum, 1);
        int size = Math.max(pageSize, 1);
        Page<T> page = new Page<>(pageIndex, size);
        List<OrderItem> orderItems = buildOrderItem();
        if (!orderItems.isEmpty()) {
            page.addOrder(orderItems);
        }
        return page;
    }

    private List<OrderItem> buildOrderItem() {
        List<OrderItem> list = new ArrayList<>();
        if (!StringUtils.hasText(orderBy)) {
            list.add(OrderItem.desc("id"));
            return list;
        }
        String[] orderByArr = orderBy.split(",");
        for (String orderByStr : orderByArr) {
            list.add(isAsc ? OrderItem.asc(orderByStr) : OrderItem.desc(orderByStr));
        }
        return list;
    }
}