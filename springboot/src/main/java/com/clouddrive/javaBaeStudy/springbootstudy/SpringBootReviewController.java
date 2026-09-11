package com.clouddrive.javaBaeStudy.springbootstudy;

import com.clouddrive.common.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.List;

/**
 * Spring Boot 复习代码，可跟随 README.md 逐段阅读。
 *
 * 接口统一前缀：/study
 * 重点：Bean、构造器注入、接口多实现、@Primary、@Qualifier、请求参数和校验。
 */
@RestController
@RequestMapping("/study")
public class SpringBootReviewController {
    // 成员变量：Controller 创建结束后，长期保存 Service。
    private final PostQueryService defaultPostQueryService;
    private final PostQueryService remotePostQueryService;
    private final StudyNotificationService notificationService;
    private final ObjectMapper objectMapper;

    /**
     * 参数是 Spring 在创建 Controller 时传入的 Bean。
     *
     * defaultPostQueryService：两个实现类都符合 PostQueryService，@Primary 选择本地实现。
     * remotePostQueryService：@Qualifier 精确指定名为 remotePostQueryService 的 Bean。
     */
    public SpringBootReviewController(
            PostQueryService defaultPostQueryService,
            @Qualifier("remotePostQueryService") PostQueryService remotePostQueryService,
            StudyNotificationService notificationService,
            ObjectMapper objectMapper
    ) {
        this.defaultPostQueryService = defaultPostQueryService;
        this.remotePostQueryService = remotePostQueryService;
        this.notificationService = notificationService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/beans/default-posts")
    public ApiResponse<List<String>> defaultPosts() {
        return ApiResponse.success(defaultPostQueryService.listPosts());
    }

    @GetMapping("/beans/remote-posts")
    public ApiResponse<List<String>> remotePosts() {
        return ApiResponse.success(remotePostQueryService.listPosts());
    }

    @PostMapping("/beans/send-sms")
    public ApiResponse<String> sendSms(@RequestParam("phone") String phone) {
        String messageId = notificationService.sendStudyMessage(phone, "Spring Bean 注入复习");
        return ApiResponse.success(messageId);
    }

    /** 示例：GET /study/posts/1111，对应 @PathVariable。 */
    @GetMapping("/posts/{id}")
    public ApiResponse<String> postDetail(@PathVariable("id") Long id) {
        return ApiResponse.success("路径中的帖子 ID：" + id);
    }

    /** 示例：GET /study/request/repeated?a=1&b=2&c=d&c=f&c=g。 */
    @GetMapping("/request/repeated")
    public ApiResponse<ListQueryRequest> repeatedRequestParams(
            @RequestParam("a") Integer a,
            @RequestParam("b") Integer b,
            @RequestParam("c") List<String> c
    ) {
        return ApiResponse.success(new ListQueryRequest(a, b, c));
    }

    /** 示例：GET /study/request/indexed?a=1&b=2&c[0]=f&c[1]=g。 */
    @GetMapping("/request/indexed")
    public ApiResponse<ListQueryRequest> indexedRequestParams(
            @ModelAttribute ListQueryRequest request
    ) {
        return ApiResponse.success(request);
    }

    /** 示例：GET /study/request/json-string?c=%5B%22d%22%2C%22f%22%2C%22g%22%5D。 */
    @GetMapping("/request/json-string")
    public ApiResponse<List<String>> jsonStringRequestParam(
            @RequestParam("c") String c
    ) throws JsonProcessingException {
        List<String> values = objectMapper.readValue(c, new TypeReference<List<String>>() {
        });
        return ApiResponse.success(values);
    }

    /**
     * 示例：POST /study/request/body。
     * 请求头：Content-Type: application/json。
     * 请求体：{ "a": 1, "b": 2, "c": ["d", "f", "g"] }。
     */
    @PostMapping("/request/body")
    public ApiResponse<ListQueryRequest> requestBody(@RequestBody ListQueryRequest request) {
        return ApiResponse.success(request);
    }

    /** @Valid 触发 DTO 上的 @NotBlank 与 @Size 规则。 */
    @PostMapping("/posts")
    public ApiResponse<CreatePostRequest> createPost(
            @Valid @RequestBody CreatePostRequest request
    ) {
        return ApiResponse.success(request);
    }
}

/** 接口：Controller 依赖能力，不依赖具体是本地还是远程实现。 */
interface PostQueryService {
    List<String> listPosts();
}

/** 数据访问层示例。真实项目里可改为 MyBatis 的 Mapper。 */
@Repository
class StudyPostRepository {
    List<String> findAll() {
        return Arrays.asList("本地帖子：Java 集合", "本地帖子：Spring Boot");
    }
}

/** @Primary：注入 PostQueryService 且未指定名称时，默认注入本类。 */
@Primary
@Service
class LocalPostQueryService implements PostQueryService {
    private final StudyPostRepository repository;

    LocalPostQueryService(StudyPostRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<String> listPosts() {
        return repository.findAll();
    }
}

/** Bean 名默认来自类名首字母小写，即 remotePostQueryService。 */
@Service
class RemotePostQueryService implements PostQueryService {
    @Override
    public List<String> listPosts() {
        return Arrays.asList("远程帖子：来自 API 的数据", "远程帖子：需要网络请求");
    }
}

/** @Component：通用 Spring 组件；本例不会真的发送短信。 */
@Component
class SmsClient {
    void send(String phone, String message) {
        System.out.println("模拟发送短信，手机号：" + phone + "，内容：" + message);
    }
}

/** @Service 的构造器依赖 @Component 和 @Bean 创建的对象。 */
@Service
class StudyNotificationService {
    private final SmsClient smsClient;
    private final IdPrefixGenerator idPrefixGenerator;

    StudyNotificationService(SmsClient smsClient, IdPrefixGenerator idPrefixGenerator) {
        this.smsClient = smsClient;
        this.idPrefixGenerator = idPrefixGenerator;
    }

    String sendStudyMessage(String phone, String message) {
        String messageId = idPrefixGenerator.nextId();
        smsClient.send(phone, "[" + messageId + "] " + message);
        return messageId;
    }
}

/** 假设这是不能修改源码的第三方类，因此不能直接在类上加 @Component。 */
class IdPrefixGenerator {
    private final String prefix;

    IdPrefixGenerator(String prefix) {
        this.prefix = prefix;
    }

    String nextId() {
        return prefix + "-" + System.currentTimeMillis();
    }
}

/** @Bean 方法返回的对象也会注册为 Spring Bean。 */
@Configuration
class StudyConfiguration {
    @Bean
    IdPrefixGenerator idPrefixGenerator() {
        return new IdPrefixGenerator("STUDY");
    }
}

/** URL 参数 DTO，也可作为 JSON Body DTO。 */
class ListQueryRequest {
    private Integer a;
    private Integer b;
    private List<String> c;

    public ListQueryRequest() {
    }

    ListQueryRequest(Integer a, Integer b, List<String> c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public Integer getA() {
        return a;
    }

    public void setA(Integer a) {
        this.a = a;
    }

    public Integer getB() {
        return b;
    }

    public void setB(Integer b) {
        this.b = b;
    }

    public List<String> getC() {
        return c;
    }

    public void setC(List<String> c) {
        this.c = c;
    }
}

/** @Valid 会检查此 DTO 字段上的 Bean Validation 注解。 */
class CreatePostRequest {
    @NotBlank(message = "标题不能为空")
    @Size(max = 100, message = "标题不能超过 100 个字符")
    private String title;

    @NotBlank(message = "内容不能为空")
    @Size(max = 500, message = "内容不能超过 500 个字符")
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
