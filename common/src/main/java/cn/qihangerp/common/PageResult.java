package cn.qihangerp.common;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {
    private long total;
    private List<T> rows;
    private int pageNum;
    private int pageSize;

    public static <T> PageResult<T> build(List<T> rows, long total) {
        PageResult<T> result = new PageResult<>();
        result.setRows(rows);
        result.setTotal(total);
        return result;
    }
}