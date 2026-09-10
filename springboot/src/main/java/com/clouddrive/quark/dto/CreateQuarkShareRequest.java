package com.clouddrive.quark.dto;

import java.util.ArrayList;
import java.util.List;

/** 创建分享。 */
public class CreateQuarkShareRequest {
    private List<String> fidList = new ArrayList<>();
    private String title = "CloudDrive 分享";
    private int urlType = 1;
    private int expiredType = 1;
    private Long expiredAt;
    private String passcode;

    public List<String> getFidList() { return fidList; }
    public void setFidList(List<String> fidList) { this.fidList = fidList; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public int getUrlType() { return urlType; }
    public void setUrlType(int urlType) { this.urlType = urlType; }
    public int getExpiredType() { return expiredType; }
    public void setExpiredType(int expiredType) { this.expiredType = expiredType; }
    public Long getExpiredAt() { return expiredAt; }
    public void setExpiredAt(Long expiredAt) { this.expiredAt = expiredAt; }
    public String getPasscode() { return passcode; }
    public void setPasscode(String passcode) { this.passcode = passcode; }
}
