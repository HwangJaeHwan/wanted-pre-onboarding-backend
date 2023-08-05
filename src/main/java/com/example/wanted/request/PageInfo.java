package com.example.wanted.request;

import lombok.Data;
import lombok.Getter;

@Data
public class PageInfo {

    private int page;

    public PageInfo() {
        this.page = 1;
    }

    public int getPage() {
        return Math.max(1, page);
    }

    public long getOffset() {
        return (long) (Math.max(1, page) - 1) * 10;
    }


}
