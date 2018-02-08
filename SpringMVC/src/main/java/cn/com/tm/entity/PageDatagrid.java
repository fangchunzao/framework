package cn.com.tm.entity;

import java.util.ArrayList;
import java.util.List;

public class PageDatagrid {

    private long total;
    private List rows = new ArrayList<>();

    public PageDatagrid() {
        super();
    }
    public PageDatagrid(long total, List rows) {
        super();
        this.total = total;
        this.rows = rows;
    }
    public long getTotal() {
        return total;
    }
    public void setTotal(long total) {
        this.total = total;
    }
    public List getRows() {
        return rows;
    }
    public void setRows(List rows) {
        this.rows = rows;
    }
}
