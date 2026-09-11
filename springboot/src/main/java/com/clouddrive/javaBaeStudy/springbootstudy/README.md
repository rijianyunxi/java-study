# Spring Boot 复习笔记

配套代码：SpringBootReviewController.java。

## 1. Spring 容器和 Bean

Spring 启动时会从启动类所在包开始扫描子包。当前启动类在 com.clouddrive，因此 com.clouddrive 下带有组件注解的类会被扫描。

Bean 就是由 Spring 创建并管理的对象。Spring 注入时不按当前包找，也不按类名字符串找；它在当前 Spring 容器中找已经注册且类型兼容的 Bean。

## 2. Component 不等于任何地方自动可用

Component 表示 Spring 创建一个 SmsClient Bean 放进容器。另一个也由 Spring 管理的类，必须在构造器中声明需要 SmsClient，Spring 创建这个类时才会注入。

~~~java
@Component
class SmsClient {
}

@Service
class StudyNotificationService {
    private final SmsClient smsClient;

    StudyNotificationService(SmsClient smsClient) {
        this.smsClient = smsClient;
    }
}
~~~

普通 Java 类，或自己手动 new 出来的对象，不会获得 Spring 自动注入。默认 Bean 是单例，所以多个 Service 默认共享容器中的同一个 SmsClient 实例。

## 3. 构造器参数和成员字段

~~~java
private final PostQueryService postQueryService;

public Demo(PostQueryService postQueryService) {
    this.postQueryService = postQueryService;
}
~~~

构造器参数只在创建对象那一刻临时存在。this.postQueryService 是对象长期保存的成员字段。赋值语句把 Spring 临时传入的对象保存起来。

final 表示这个字段只能赋值一次；不代表它指向的对象内部所有数据完全不能修改。

TS 可以在构造器中用 private readonly 简写声明字段；Java 要像上面一样单独声明字段并赋值。

## 4. 接口、多实现、Primary、Qualifier

PostQueryService 是接口。LocalPostQueryService 和 RemotePostQueryService 都 implements 它，所以二者都能当作 PostQueryService 使用。这就是多态。

如果构造器需要 PostQueryService，而容器里恰好有两个实现 Bean，Spring 不知道选哪个。

- Primary：当没有指定 Bean 名时，选择默认实现。
- Qualifier：在当前构造器参数上精确指定需要哪个 Bean。

~~~java
@Primary
@Service
class LocalPostQueryService implements PostQueryService {
}

@Service
class RemotePostQueryService implements PostQueryService {
}

public Demo(
        PostQueryService defaultService,
        @Qualifier("remotePostQueryService") PostQueryService remoteService
) {
}
~~~

Primary 不会替代成员字段。Qualifier 只负责告诉 Spring 选哪个对象。

项目原本的 com.clouddrive.example.service.PostService 是普通 Service 类，目前只有一个，因此它不需要 Primary 或 Qualifier。

## 5. 常见组件注解

| 注解 | 放置位置 | 典型职责 |
| --- | --- | --- |
| Component | 类 | 通用组件，例如客户端、工具类 |
| Service | 类 | 业务逻辑 |
| Repository | 类 | 数据访问层 |
| Controller | 类 | 传统页面 Controller |
| RestController | 类 | 前后端分离 JSON 接口 |
| Configuration | 类 | 放置 Bean 方法的配置类 |
| Bean | 方法 | 将返回值注册为 Spring Bean |

RestController 可近似理解为 Controller 加 ResponseBody，方法返回的对象会序列化成 JSON 响应。

Bean 适合注册第三方类，或对象创建过程需要写配置、传参数的情形。示例中的 IdPrefixGenerator 就由 StudyConfiguration 的 Bean 方法创建。

## 6. URL 参数

PathVariable 用于路径里的资源标识：

~~~text
GET /study/posts/1111
~~~

~~~java
@GetMapping("/posts/{id}")
public Object detail(@PathVariable("id") Long id) {
}
~~~

RequestParam 用于问号后的查询条件：

~~~text
GET /study/posts?keyword=java&page=2
~~~

一般记忆：PathVariable 是指出是哪一条资源，RequestParam 是描述怎样查询。

## 7. 数组查询参数

推荐同名参数重复：

~~~text
GET /study/request/repeated?a=1&b=2&c=d&c=f&c=g
~~~

~~~java
@RequestParam("c") List<String> c
~~~

前端若发 c[0]=f 和 c[1]=g，使用 DTO 加 ModelAttribute：

~~~text
GET /study/request/indexed?a=1&b=2&c[0]=f&c[1]=g
~~~

~~~java
public Object indexed(@ModelAttribute ListQueryRequest request) {
}
~~~

如果传 c=["d","f","g"]，HTTP 看来它只是一个字符串。先用 RequestParam String c 接收，再由 ObjectMapper 解析。业务上不推荐把 JSON 再塞进 URL；复杂结构优先发 JSON Body。

## 8. RequestBody

RequestBody 从请求体读取 JSON，并交给 Jackson 自动转换为 DTO。

~~~http
POST /study/request/body
Content-Type: application/json
~~~

~~~json
{
  "a": 1,
  "b": 2,
  "c": ["d", "f", "g"]
}
~~~

~~~java
@PostMapping("/request/body")
public Object body(@RequestBody ListQueryRequest request) {
}
~~~

一般选择：GET 的列表、搜索、分页用 RequestParam；查一个明确资源用 PathVariable；新增、修改、复杂 JSON 用 RequestBody DTO。

## 9. Valid、NotBlank、Size

CreatePostRequest 中的 NotBlank 和 Size 是参数校验规则。Controller 参数上的 Valid 才会触发这些规则。

~~~java
class CreatePostRequest {
    @NotBlank(message = "标题不能为空")
    @Size(max = 100, message = "标题不能超过 100 个字符")
    private String title;
}

@PostMapping("/posts")
public Object create(@Valid @RequestBody CreatePostRequest request) {
}
~~~

NotBlank 表示字符串不能为 null、空字符串或全空格。Size(max = 100) 表示字符串或集合长度最多 100。校验失败后，Controller 方法不会正常执行，Spring 会返回 400。

当前项目已经在 pom.xml 添加 spring-boot-starter-validation。因为项目是 Spring Boot 2.7，所以这些注解的包名是 javax.validation。

## 10. 可直接测试的接口

| 方法 | URL | 复习点 |
| --- | --- | --- |
| GET | /study/beans/default-posts | Primary 默认注入 |
| GET | /study/beans/remote-posts | Qualifier 精确注入 |
| POST | /study/beans/send-sms?phone=13800000000 | Component 和构造器注入 |
| GET | /study/posts/1111 | PathVariable |
| GET | /study/request/repeated?a=1&b=2&c=d&c=f&c=g | RequestParam 数组 |
| GET | /study/request/indexed?a=1&b=2&c[0]=f&c[1]=g | ModelAttribute DTO |
| GET | /study/request/json-string?c=%5B%22d%22%2C%22f%22%2C%22g%22%5D | JSON 字符串参数 |
| POST | /study/request/body | RequestBody JSON |
| POST | /study/posts | Valid 与 DTO 校验 |

## 11. 接下来的学习顺序

1. MyBatis 或 MyBatis-Plus：Controller 到 Service 到 Mapper 到 MySQL。
2. DTO、Entity、VO 的职责边界。
3. RestControllerAdvice：统一异常 JSON。
4. Transactional：多次数据库操作的事务与回滚。
5. JWT、拦截器或 Spring Security：登录鉴权。
