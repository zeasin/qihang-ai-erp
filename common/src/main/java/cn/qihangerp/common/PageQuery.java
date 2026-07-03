package cn.qihangerp.common;

import lombok.Data;

@Data
public class PageQuery {
    private int pageNum = 1;
    private int pageSize = 20;
    private String orderBy;
    private boolean isAsc = true;
}