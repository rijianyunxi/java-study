package com.clouddrive.common;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Java 主动发 HTTP 请求的复习示例。
 *
 * <p>本项目使用 Spring Boot 2.7 + Java 8，因此这里使用 RestTemplate。
 * RestClient 是 Spring Framework 6.1（通常对应 Spring Boot 3 / Java 17）才有的 API。</p>
 *
 * <p>把 BASE_URL、token、文件路径，以及 /api/... 接口路径替换为你实际的接口即可。</p>
 *
 * <p>注意：main 方法默认不会执行网络请求，避免直接运行时请求不存在的 localhost 接口。
 * 想测试时，取消某个方法调用的注释。</p>
 */
public class HttpRequestStudy {

    private static final String BASE_URL = "http://localhost:8080";

    private final RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        HttpRequestStudy demo = new HttpRequestStudy();

        // 取消注释后再执行；每个方法都会真的发起 HTTP 请求。
        // demo.getWithQueryString("张三", 1, 10, "your-token");
        // demo.getWithUriBuilder("张三 李四", 1, 10, "your-token");
        // demo.getById(1001L, "your-token");
        // demo.postJson("your-token");
        // demo.postUrlEncodedForm();
        // demo.postMultipartDirectly();
        // demo.postMultipartWithBuilder();

        System.out.println("这是 HTTP 请求复习代码。请先修改 BASE_URL，再取消 main 中要测试的方法注释。");
    }

    /**
     * GET + 手动拼接 Query String。
     *
     * <p>请求：GET /api/users?keyword=张三&page=1&size=10</p>
     *
     * <p>适合参数都是数字等简单值。若 keyword 是用户输入，里面可能含有空格、&、?、中文，
     * 推荐改用下面的 getWithUriBuilder 方法，让 Spring 负责 URL 编码。</p>
     */
    public Map<String, Object> getWithQueryString(String keyword, int page, int size, String token) {
        String url = BASE_URL
                + "/api/users?keyword=" + keyword
                + "&page=" + page
                + "&size=" + size;

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<Void>(createHeaders(token)),
                new ParameterizedTypeReference<Map<String, Object>>() {
                }
        );

        return response.getBody();
    }

    /**
     * GET + UriComponentsBuilder（推荐的 Query 参数写法）。
     *
     * <p>最终 URL 类似：/api/users?keyword=张三%20李四&page=1&size=10</p>
     */
    public Map<String, Object> getWithUriBuilder(String keyword, int page, int size, String token) {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path("/api/users")
                .queryParam("keyword", keyword)
                .queryParam("page", page)
                .queryParam("size", size)
                .encode()
                .toUriString();

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<Void>(createHeaders(token)),
                new ParameterizedTypeReference<Map<String, Object>>() {
                }
        );

        return response.getBody();
    }

    /**
     * GET + 路径参数：GET /api/users/{id}，例如 GET /api/users/1001。
     *
     * <p>最后的 id 参数会自动替换 URL 中的 {id}。</p>
     */
    public Map<String, Object> getById(Long id, String token) {
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                BASE_URL + "/api/users/{id}",
                HttpMethod.GET,
                new HttpEntity<Void>(createHeaders(token)),
                new ParameterizedTypeReference<Map<String, Object>>() {
                },
                id
        );

        return response.getBody();
    }

    /**
     * 一个完整的 JSON POST 请求。
     *
     * <p>请求头：</p>
     * <pre>
     * Content-Type: application/json
     * Authorization: Bearer your-token
     * X-Request-Id: request-001
     * </pre>
     *
     * <p>请求 JSON：</p>
     * <pre>
     * {"name":"张三","age":20,"email":"zhangsan@example.com"}
     * </pre>
     *
     * <p>响应 JSON 会由 Jackson 自动解析为 Map&lt;String, Object&gt;。</p>
     */
    public Map<String, Object> postJson(String token) {
        // Java 8 没有 Map.of，所以用 HashMap。
        Map<String, Object> requestBody = new HashMap<String, Object>();
        requestBody.put("name", "张三");
        requestBody.put("age", 20);
        requestBody.put("email", "zhangsan@example.com");

        HttpHeaders headers = createHeaders(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Request-Id", "request-001");
        headers.set("X-Client", "springboot-study");

        // HttpEntity = 请求头 + 请求体。
        HttpEntity<Map<String, Object>> request = new HttpEntity<Map<String, Object>>(requestBody, headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                BASE_URL + "/api/users",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<Map<String, Object>>() {
                }
        );

        return response.getBody();
    }

    /**
     * 传统普通表单：application/x-www-form-urlencoded。
     *
     * <p>请求体大致是：username=zhangsan&password=123456&rememberMe=true</p>
     * <p>适合登录等普通文本字段；不适合上传文件。</p>
     */
    public Map<String, Object> postUrlEncodedForm() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
        formData.add("username", "zhangsan");
        formData.add("password", "123456");
        formData.add("rememberMe", "true");

        HttpHeaders headers = new HttpHeaders();
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

    /**
     * multipart/form-data：直接使用 LinkedMultiValueMap 的写法。
     *
     * <p>这和前端的 FormData 很接近：</p>
     * <pre>
     * formData.append("name", "张三");
     * formData.append("avatar", file);
     * </pre>
     *
     * <p>MultiValueMap 的 value 必须是 Object：文本是 String，文件是 FileSystemResource。</p>
     */
    public Map<String, Object> postMultipartDirectly() {
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<String, Object>();
        formData.add("name", "张三");
        formData.add("age", "20");

        // 换成自己电脑上真实存在的文件路径。
        FileSystemResource avatar = new FileSystemResource("/Users/song/Desktop/avatar.png");
        formData.add("avatar", avatar);

        HttpHeaders headers = new HttpHeaders();
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

    /**
     * multipart/form-data：MultipartBodyBuilder 写法。
     *
     * <p>它可以方便地为每个 part 单独设置文件名、Content-Type 等信息。
     * 直接 LinkedMultiValueMap 和 MultipartBodyBuilder 都是可用写法；后者更适合复杂上传。</p>
     */
    public Map<String, Object> postMultipartWithBuilder() {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("name", "张三");
        builder.part("age", 20);

        FileSystemResource avatar = new FileSystemResource("/Users/song/Desktop/avatar.png");
        builder.part("avatar", avatar)
                .filename("my-avatar.png")
                .contentType(MediaType.IMAGE_PNG);

        HttpHeaders headers = new HttpHeaders();
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

    /**
     * 创建公共请求头。
     * HttpHeaders 的 set 表示：该 Header 只保留这一个值；若已有旧值则覆盖。
     */
    private HttpHeaders createHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();

        if (token != null && !token.trim().isEmpty()) {
            headers.setBearerAuth(token);
        }

        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }
}
