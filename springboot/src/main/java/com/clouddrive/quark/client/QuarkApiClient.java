package com.clouddrive.quark.client;

import com.clouddrive.quark.config.QuarkProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

/**
 * 夸克上游 HTTP 客户端。
 *
 * 这里集中处理浏览器伪装请求头、公共 query 参数和 Cookie，业务 Service 不重复写这些细节。
 */
@Component
public class QuarkApiClient {

    private static final String USER_AGENT =
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) " +
                    "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/139.0.0.0 Safari/537.36";

    private final RestTemplate restTemplate;
    private final QuarkProperties properties;

    public QuarkApiClient(RestTemplate restTemplate, QuarkProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    public ResponseEntity<String> get(String baseUrl, String path,
                                      Map<String, String> query, String requestCookie) {
        return exchange(baseUrl, path, HttpMethod.GET, query, null, requestCookie);
    }

    public ResponseEntity<String> post(String baseUrl, String path,
                                       Map<String, String> query, Object body,
                                       String requestCookie) {
        return exchange(baseUrl, path, HttpMethod.POST, query, body, requestCookie);
    }

    private ResponseEntity<String> exchange(String baseUrl, String path,
                                            HttpMethod method,
                                            Map<String, String> query,
                                            Object body,
                                            String requestCookie) {
        URI uri = buildUri(baseUrl, path, query);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("User-Agent", USER_AGENT);
        headers.set("Origin", "https://pan.quark.cn");
        headers.set("Referer", "https://pan.quark.cn/");
        headers.set("Accept-Language", "zh-CN,zh;q=0.9");

        String cookie = StringUtils.hasText(requestCookie)
                ? requestCookie.trim()
                : properties.getCookie();
        if (StringUtils.hasText(cookie)) {
            headers.set(HttpHeaders.COOKIE, cookie);
        }

        try {
            ResponseEntity<String> upstream = restTemplate.exchange(
                    uri, method, new HttpEntity<Object>(body, headers), String.class);
            return withoutHopByHopHeaders(upstream);
        } catch (Exception ex) {
            throw new QuarkProxyException("无法连接夸克上游接口：" + uri.getPath(), ex);
        }
    }

    /**
     * RestTemplate 已经把上游响应体读完了，不能再把上游的 Transfer-Encoding /
     * Content-Length 原样交给本地 Tomcat，否则本地服务器可能再次分块编码，导致
     * 浏览器看到 "chunk hex-length" 之类的解析错误。
     */
    private ResponseEntity<String> withoutHopByHopHeaders(ResponseEntity<String> upstream) {
        HttpHeaders safeHeaders = new HttpHeaders();
        upstream.getHeaders().forEach((name, values) -> {
            if (!isHopByHopHeader(name)) {
                safeHeaders.put(name, values);
            }
        });
        return new ResponseEntity<>(upstream.getBody(), safeHeaders, upstream.getStatusCode());
    }

    private boolean isHopByHopHeader(String name) {
        String lowerName = name.toLowerCase();
        return "connection".equals(lowerName)
                || "keep-alive".equals(lowerName)
                || "proxy-authenticate".equals(lowerName)
                || "proxy-authorization".equals(lowerName)
                || "te".equals(lowerName)
                || "trailer".equals(lowerName)
                || "transfer-encoding".equals(lowerName)
                || "upgrade".equals(lowerName)
                || "content-length".equals(lowerName);
    }

    private URI buildUri(String baseUrl, String path, Map<String, String> query) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(trimEndSlash(baseUrl) + "/" + trimStartSlash(path))
                .queryParam("pr", "ucpro")
                .queryParam("fr", "pc")
                .queryParam("uc_param_str", "")
                .queryParam("__t", System.currentTimeMillis())
                .queryParam("__dt", "1000");

        if (query != null) {
            for (Map.Entry<String, String> entry : query.entrySet()) {
                if (entry.getValue() != null) {
                    builder.queryParam(entry.getKey(), entry.getValue());
                }
            }
        }
        return builder.build().encode().toUri();
    }

    private String trimEndSlash(String value) {
        return value == null ? "" : value.replaceAll("/+$", "");
    }

    private String trimStartSlash(String value) {
        return value == null ? "" : value.replaceAll("^/+", "");
    }
}
