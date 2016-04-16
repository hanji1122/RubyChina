package com.luckyhan.rubychina.model;

public class Page {

    public static final int LIMIT = 20;
    private int page;
    private int lastSuccess;

    public Page() {
        page = 0;
    }

    public Page(int page) {
        this.page = page;
    }

    public int nextPage() {
        page += LIMIT;
        return page;
    }

    public int current() {
        return page;
    }

    public int refresh() {
        page = 0;
        return page;
    }

    public int refresh(int page) {
        this.page = page;
        return page;
    }

    public boolean isTop() {
        return page == 0;
    }

    public boolean isTop(int top) {
        return page == top;
    }

    public void setLastSuccess() {
        lastSuccess = page;
    }

    public void recover() {
        page = lastSuccess;
    }

    @Override
    public String toString() {
        return String.valueOf(page);
    }


}
