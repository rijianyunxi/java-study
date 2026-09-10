package com.clouddrive.javaBaeStudy.http;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/** PUT：更新 /api/users/{id}，请求体和响应体都是 JSON。 */
public class PutJsonDemo {

    private static final String BASE_URL = "http://localhost:8080";
    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> send(Long id, String token) {
        Map<String, Object> requestBody = new HashMap<String, Object>();
        requestBody.put("name", "李四");
        requestBody.put("age", 21);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<Map<String, Object>>(requestBody, headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                BASE_URL + "/api/users/{id}",
                HttpMethod.PUT,
                request,
                new ParameterizedTypeReference<Map<String, Object>>() {
                },
                id
        );

        return response.getBody();
    }
}
