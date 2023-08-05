package com.example.wanted.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PostList {

    private int totalPage;

    private List<PostListInfo> infos;

    private Boolean isFirst;

    private Boolean isLast;


    @Builder
    public PostList(int totalPage, List<PostListInfo> infos, Boolean isFirst, Boolean isLast) {
        this.totalPage = totalPage;
        this.infos = infos;
        this.isFirst = isFirst;
        this.isLast = isLast;
    }

}
