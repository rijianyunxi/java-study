package com.clouddrive.common.study_demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;


import java.util.HashMap;
import java.util.Map;


public class RequestCPA {
    private static final String BASE_URL = "http://localhost:8080/api/demo/folders";
    private static final RestTemplate restTemplate = new RestTemplate();


    public static void main(String[] args) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("biubiul.1234");
        headers.set("Accept",MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String,Object> data = new HashMap<>();
        data.put("name","学习资料");
        data.put("parentId",0);
        HttpEntity<Map<String,Object>> httpEntity = new HttpEntity<>(data,headers);

        ResponseEntity<Map<String,Object>> response = restTemplate.exchange(
                BASE_URL,
                HttpMethod.POST,
                httpEntity,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        ObjectMapper objectMapper = new ObjectMapper();
        String prettyJson = objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(response.getBody());

        System.out.println(prettyJson);
    }
}
