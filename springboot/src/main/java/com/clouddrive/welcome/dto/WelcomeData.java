package com.clouddrive.welcome.dto;

/** 接口返回的业务数据，对应 JSON 中的 data 对象。 */
public class WelcomeData {

    private final String text;

    public WelcomeData(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
