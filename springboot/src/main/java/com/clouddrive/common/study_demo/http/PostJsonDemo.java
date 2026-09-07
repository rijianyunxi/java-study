package com.clouddrive.common.study_demo.http;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/** POST：请求体发送 JSON，响应 JSON 自动解析为 Map<String, Object>。 */
public class PostJsonDemo {

    private static final String BASE_URL = "http://localhost:8080";
    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> send(String token) {
        // Java 8 没有 Map.of，所以使用 HashMap。
        Map<String, Object> requestBody = new HashMap<String, Object>();
        requestBody.put("name", "张三");
        requestBody.put("age", 20);
        requestBody.put("email", "zhangsan@example.com");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Request-Id", "request-001");
        headers.set("X-Client", "springboot-study");

        // HttpEntity = 请求头 headers + 请求体 requestBody。
        HttpEntity<Map<String, Object>> request =
                new HttpEntity<Map<String, Object>>(requestBody, headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                BASE_URL + "/api/users",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<Map<String, Object>>() {
                }
        );

        return response.getBody();
    }
}
