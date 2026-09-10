package com.clouddrive.quark.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 夸克网盘上游地址和默认 Cookie 配置。
 *
 * Cookie 不写进代码，也不提交到 Git；可以通过环境变量 QUARK_COOKIE 注入。
 */
@Component
@ConfigurationProperties(prefix = "quark")
public class QuarkProperties {

    private String apiBaseUrl = "https://drive-pc.quark.cn/1/clouddrive";
    private String shareBaseUrl = "https://drive.quark.cn/1/clouddrive";
    private String authBaseUrl = "https://uop.quark.cn";
    private String cookie = "";

    public String getApiBaseUrl() {
        return apiBaseUrl;
    }

    public void setApiBaseUrl(String apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
    }

    public String getShareBaseUrl() {
        return shareBaseUrl;
    }

    public void setShareBaseUrl(String shareBaseUrl) {
        this.shareBaseUrl = shareBaseUrl;
    }

    public String getAuthBaseUrl() {
        return authBaseUrl;
    }

    public void setAuthBaseUrl(String authBaseUrl) {
        this.authBaseUrl = authBaseUrl;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }
}
