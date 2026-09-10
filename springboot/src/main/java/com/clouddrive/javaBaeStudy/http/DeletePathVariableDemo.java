package com.clouddrive.javaBaeStudy.http;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/** DELETE：删除 /api/users/{id}，并将服务端的 JSON 响应解析为 Map。 */
public class DeletePathVariableDemo {

    private static final String BASE_URL = "http://localhost:8080";
    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> send(Long id, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                BASE_URL + "/api/users/{id}",
                HttpMethod.DELETE,
                new HttpEntity<Void>(headers),
                new ParameterizedTypeReference<Map<String, Object>>() {
                },
                id
        );

        return response.getBody();
    }
}
