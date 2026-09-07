package com.clouddrive.common.study_demo.http;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/** POST：multipart/form-data，直接使用 LinkedMultiValueMap，类似前端 FormData。 */
public class PostMultipartDirectDemo {

    private static final String BASE_URL = "http://localhost:8080";
    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> send() {
        // value 是 Object：普通字段是 String，文件字段是 FileSystemResource。
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<String, Object>();
        formData.add("name", "张三");
        formData.add("age", "20");

        // 换成自己电脑上真实存在的文件路径。
        FileSystemResource avatar = new FileSystemResource("/Users/song/Desktop/avatar.png");
        formData.add("avatar", avatar);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> request =
                new HttpEntity<MultiValueMap<String, Object>>(formData, headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                BASE_URL + "/api/users/upload",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<Map<String, Object>>() {
                }
        );

        return response.getBody();
    }
}
