package com.clouddrive.example.dto;

/** 列表中的一条帖子数据，仅包含帖子编号和标题。 */
public class PostItem {

    private final long id;
    private final String title;

    public PostItem(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
