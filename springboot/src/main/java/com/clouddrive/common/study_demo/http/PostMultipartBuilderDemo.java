package com.clouddrive.common.study_demo.http;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/** POST：multipart/form-data，MultipartBodyBuilder 适合设置文件名、单个 part 的类型等复杂需求。 */
public class PostMultipartBuilderDemo {

    private static final String BASE_URL = "http://localhost:8080";
    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> send() {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("name", "张三");
        builder.part("age", 20);

        // 换成自己电脑上真实存在的文件路径。
        FileSystemResource avatar = new FileSystemResource("/Users/song/Desktop/avatar.png");
        builder.part("avatar", avatar)
                .filename("my-avatar.png")
                .contentType(MediaType.IMAGE_PNG);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, HttpEntity<?>>> request =
                new HttpEntity<MultiValueMap<String, HttpEntity<?>>>(builder.build(), headers);

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
