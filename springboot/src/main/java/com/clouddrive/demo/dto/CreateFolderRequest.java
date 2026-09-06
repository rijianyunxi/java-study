package com.clouddrive.demo.dto;

/**
 * 演示 @RequestBody：接收客户端提交的 JSON。
 * Jackson 会通过无参构造器和 setter，把 JSON 字段填入这个对象。
 */
public class CreateFolderRequest {

    private String name;
    private Long parentId;

    public CreateFolderRequest() {
        // JSON 反序列化需要无参构造器。
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
