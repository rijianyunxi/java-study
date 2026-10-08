package com.clouddrive.example.dto;

/** 列表中的一条帖子数据，仅包含帖子编号和标题。 */
public class PostDetail {

    private final long id;
    private final String title;
    private final String time;

    public PostDetail(long id, String title,String time) {
        this.id = id;
        this.title = title;
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }
}
