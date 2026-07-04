package cn.qihangerp.api.ai;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.model.OGoods;
import cn.qihangerp.model.OGoodsSkuStock;
import cn.qihangerp.model.OOrder;
import cn.qihangerp.service.OGoodsService;
import cn.qihangerp.service.OGoodsSkuStockService;
import cn.qihangerp.service.OOrderService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * AI 可调用的业务工具集：把 ERP 的订单/商品/库存查询能力暴露给大模型，
 * 使其能通过自然语言完成"查订单、查商品、查库存"等操作。
 */
@Component
public class ErpTools {

    @Autowired
    private OOrderService orderService;
    @Autowired
    private OGoodsService goodsService;
    @Autowired
    private OGoodsSkuStockService stockService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Tool(description = "查询订单列表。可按订单号(模糊)、收货人姓名(模糊)、订单状态、下单时间范围筛选。" +
            "订单状态：0=待付款，1=已付款待发货，2=已发货，3=已完成，4=已取消。" +
            "startTime/endTime 格式 'YYYY-MM-DD HH:mm:ss'。返回订单概要列表(最多10条)。")
    public Map<String, Object> queryOrders(
            @ToolParam(description = "订单号关键字，模糊匹配，可为空") String orderNum,
            @ToolParam(description = "收货人姓名关键字，模糊匹配，可为空") String receiverName,
            @ToolParam(description = "订单状态：0待付款/1已付款待发货/2已发货/3已完成/4已取消，可为空") Integer orderStatus,
            @ToolParam(description = "开始时间，格式 YYYY-MM-DD HH:mm:ss，可为空") String startTime,
            @ToolParam(description = "结束时间，格式 YYYY-MM-DD HH:mm:ss，可为空") String endTime) {
        PageQuery pq = new PageQuery();
        pq.setPageNum(1);
        pq.setPageSize(10);
        PageResult<OOrder> result = orderService.list(orderNum, receiverName, orderStatus, startTime, endTime, pq);
        List<Map<String, Object>> rows = result.getRows().stream().map(this::orderSummary).toList();
        Map<String, Object> ret = new LinkedHashMap<>();
        ret.put("total", result.getTotal());
        ret.put("orders", rows);
        return ret;
    }

    @Tool(description = "按订单ID查询单个订单详情，包含订单明细(商品行)。id 为订单主键。")
    public Map<String, Object> getOrderById(@ToolParam(description = "订单主键ID") Long id) {
        OOrder o = orderService.getById(id);
        if (o == null) {
            return Map.of("found", false, "message", "订单不存在: " + id);
        }
        Map<String, Object> ret = orderSummary(o);
        if (o.getItemList() != null) {
            List<Map<String, Object>> items = o.getItemList().stream().map(it -> {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("goodsTitle", it.getGoodsTitle());
                m.put("skuNum", it.getSkuNum());
                m.put("goodsSpec", it.getGoodsSpec());
                m.put("quantity", it.getQuantity());
                m.put("goodsPrice", it.getGoodsPrice());
                m.put("itemAmount", it.getItemAmount());
                return m;
            }).toList();
            ret.put("items", items);
        }
        return ret;
    }

    @Tool(description = "查询商品列表，按商品名称模糊匹配。返回商品概要(最多10条)。")
    public Map<String, Object> queryGoods(
            @ToolParam(description = "商品名称关键字，模糊匹配，可为空") String name) {
        PageQuery pq = new PageQuery();
        pq.setPageNum(1);
        pq.setPageSize(10);
        PageResult<OGoods> result = goodsService.list(name, null, null, null, pq);
        List<Map<String, Object>> rows = result.getRows().stream().map(g -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", g.getId());
            m.put("name", g.getName());
            m.put("goodsNum", g.getGoodsNum());
            m.put("barCode", g.getBarCode());
            m.put("unitName", g.getUnitName());
            m.put("retailPrice", g.getRetailPrice());
            m.put("status", g.getStatus());
            return m;
        }).toList();
        Map<String, Object> ret = new LinkedHashMap<>();
        ret.put("total", result.getTotal());
        ret.put("goods", rows);
        return ret;
    }

    @Tool(description = "查询SKU库存。可按商品名称、SKU编码、SKU名称模糊匹配。返回库存列表(最多20条)，包含可用库存数量。")
    public Map<String, Object> queryStock(
            @ToolParam(description = "关键字，匹配商品名/SKU编码/SKU名称，可为空") String keyword) {
        PageQuery pq = new PageQuery();
        pq.setPageNum(1);
        pq.setPageSize(20);
        PageResult<OGoodsSkuStock> result = stockService.list(keyword, pq);
        List<Map<String, Object>> rows = result.getRows().stream().map(s -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("skuId", s.getSkuId());
            m.put("skuCode", s.getSkuCode());
            m.put("goodsName", s.getGoodsName());
            m.put("skuName", s.getSkuName());
            m.put("quantity", s.getQuantity());
            return m;
        }).toList();
        Map<String, Object> ret = new LinkedHashMap<>();
        ret.put("total", result.getTotal());
        ret.put("stocks", rows);
        return ret;
    }

    @Tool(description = "执行只读SQL查询并返回结果。只能执行SELECT语句，不可修改数据。返回结果最多100行。")
    public List<Map<String, Object>> queryBySql(
            @ToolParam(description = "只读SQL查询语句，仅限SELECT。如果未指定ORDER BY则自动按id倒序。") String sql) {
        String trimmed = sql.trim();
        if (!trimmed.toUpperCase().startsWith("SELECT")) {
            throw new RuntimeException("只允许执行 SELECT 查询");
        }
        if (trimmed.toUpperCase().contains("INTO ")) {
            throw new RuntimeException("不允许 SELECT INTO");
        }
        String finalSql = trimmed;
        if (!trimmed.toUpperCase().contains("LIMIT")) {
            finalSql = trimmed.replaceAll(";\\s*$", "") + " LIMIT 100";
        }
        return jdbcTemplate.query(finalSql, (rs, rowNum) -> {
            Map<String, Object> row = new LinkedHashMap<>();
            var meta = rs.getMetaData();
            int count = meta.getColumnCount();
            for (int i = 1; i <= count; i++) {
                row.put(meta.getColumnLabel(i), rs.getObject(i));
            }
            return row;
        });
    }

    private Map<String, Object> orderSummary(OOrder o) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", o.getId());
        m.put("orderNum", o.getOrderNum());
        m.put("orderStatus", o.getOrderStatus());
        m.put("receiverName", o.getReceiverName());
        m.put("receiverMobile", o.getReceiverMobile());
        m.put("amount", o.getAmount());
        m.put("payment", o.getPayment());
        m.put("orderTime", o.getOrderTime());
        m.put("logisticsCompany", o.getLogisticsCompany());
        m.put("logisticsNo", o.getLogisticsNo());
        return m;
    }
}
