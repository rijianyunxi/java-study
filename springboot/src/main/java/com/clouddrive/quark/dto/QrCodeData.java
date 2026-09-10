package com.clouddrive.quark.dto;

/** 前端扫码登录面板使用的数据。 */
public class QrCodeData {
    private String token;
    private String qrUrl;
    private String upstream;

    public QrCodeData() {
    }

    public QrCodeData(String token, String qrUrl, String upstream) {
        this.token = token;
        this.qrUrl = qrUrl;
        this.upstream = upstream;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getQrUrl() { return qrUrl; }
    public void setQrUrl(String qrUrl) { this.qrUrl = qrUrl; }
    public String getUpstream() { return upstream; }
    public void setUpstream(String upstream) { this.upstream = upstream; }
}
