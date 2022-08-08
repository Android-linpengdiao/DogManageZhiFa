package com.okhttp;

import java.io.Serializable;
import java.util.List;

public class Pager<T> implements Serializable {
    private static final long serialVersionUID = -3957020823257107332L;
    private int cursor = 1;
    private int size = 20;
    private List<T> rows;
    private long total;

    //ResultClient 数据
    private boolean success = true;
    private String msg;
    private int code;

    public Pager() {
    }

    public Pager(int size) {
        this.size = size;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCursor() {
        return this.cursor;
    }

    public void setCursor(int cursor) {
        this.cursor = cursor;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public boolean isSuccess() {
        return code == 200;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
