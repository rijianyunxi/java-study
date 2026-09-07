package com.clouddrive.common.study_demo.http;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

/** GET：使用 UriComponentsBuilder 构建 Query 参数，Spring 会处理 URL 编码。 */
public class GetUriBuilderDemo {

    private static final String BASE_URL = "http://localhost:8080";
    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> send(String keyword, int page, int size, String token) {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path("/api/users")
                .queryParam("keyword", keyword)
                .queryParam("page", page)
                .queryParam("size", size)
                .encode()
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<Void>(headers),
                new ParameterizedTypeReference<Map<String, Object>>() {
                }
        );

        return response.getBody();
    }
}
