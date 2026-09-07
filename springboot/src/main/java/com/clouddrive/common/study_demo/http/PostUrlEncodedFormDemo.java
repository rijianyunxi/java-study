package com.clouddrive.common.study_demo.http;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/** POST：传统普通表单 application/x-www-form-urlencoded。 */
public class PostUrlEncodedFormDemo {

    private static final String BASE_URL = "http://localhost:8080";
    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> send() {
        // 实际请求体大致为：username=zhangsan&password=123456&rememberMe=true
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
        formData.add("username", "zhangsan");
        formData.add("password", "123456");
        formData.add("rememberMe", "true");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<MultiValueMap<String, String>>(formData, headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                BASE_URL + "/api/login",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<Map<String, Object>>() {
                }
        );

        return response.getBody();
    }
}
