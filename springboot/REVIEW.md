# CloudDrive Spring Boot 2.x 复习速查

> 适合在继续写代码前，用 5～10 分钟快速回顾。更完整的说明见同目录 `README.md`，当天学习过程见仓库根目录 `learning-log/2026-09-06-spring-boot-2x.md`。

## 1. 怎么启动

```bash
cd /Users/song/study/java-study/springboot
mvn spring-boot:run
```

成功标志：日志中出现 `Started CloudDriveApplication`。

打开浏览器测试页：

```text
http://localhost:8080/
```

停止服务：启动终端按 `Ctrl + C`。

## 2. 项目入口在哪里

```text
src/main/java/com/clouddrive/CloudDriveApplication.java
```

```java
@SpringBootApplication
public class CloudDriveApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloudDriveApplication.class, args);
    }
}
```

- `main()`：Java 程序入口。
- `@SpringBootApplication`：启用 Spring Boot 配置、自动配置、组件扫描。
- 启动类在 `com.clouddrive`，其子包中的 `@RestController`、`@Service` 默认会被扫描。

## 3. 当前分包方式

```text
com.clouddrive
├── common/      跨模块共用：ApiResponse
├── welcome/     欢迎接口示例
├── post/        帖子列表示例
└── demo/        参数解析示例
```

每个业务模块可以再分：

```text
业务模块/
├── controller/  HTTP 接口入口
├── service/     业务逻辑
└── dto/         输入或输出的数据结构
```

不是每一个接口都必须创建三个类。简单接口可以只有 Controller 方法；有业务规则再增加 Service；需要结构化请求或响应时再增加 DTO。

## 4. 一次请求怎么流转

```text
浏览器 GET /api/list
  ↓
Tomcat 接收 HTTP 请求
  ↓
Spring 根据 @GetMapping 找到 PostController 方法
  ↓
Controller 调用 PostService
  ↓
Service 返回 List<PostItem>
  ↓
ApiResponse.success(...) 包装数据
  ↓
Jackson 把 Java 对象序列化为 JSON
  ↓
浏览器获得响应
```

## 5. 常用注解

| 注解 | 作用 | 示例 |
| --- | --- | --- |
| `@RestController` | 声明 JSON API Controller | 类上使用 |
| `@RequestMapping("/api")` | 设置公共路径前缀 | 类或方法上使用 |
| `@GetMapping` | 接收 GET 请求 | `@GetMapping("/files")` |
| `@PostMapping` | 接收 POST 请求 | `@PostMapping("/folders")` |
| `@DeleteMapping` | 接收 DELETE 请求 | `@DeleteMapping("/files/{id}")` |
| `@RequestParam` | 读取 `?page=1` | `@RequestParam int page` |
| `@PathVariable` | 读取 `/files/123` 中的值 | `@PathVariable Long id` |
| `@RequestBody` | 把 JSON 变成 DTO | `@RequestBody CreateFolderRequest request` |
| `@RequestHeader` | 读取请求头 | `@RequestHeader("X-Client-Name") String name` |
| `@Service` | 让 Spring 管理业务类 | Service 类上使用 |

## 6. 公共响应与泛型

```java
public class ApiResponse<T> {
    private final int status;
    private final boolean result;
    private final String msg;
    private final T data;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(0, true, "", data);
    }
}
```

| 写法 | 含义 |
| --- | --- |
| `ApiResponse<T>` | `data` 的类型由 `T` 决定 |
| `ApiResponse<String>` | `data` 是字符串 |
| `ApiResponse<List<PostItem>>` | `data` 是帖子列表，JSON 中表现为数组 |
| 方法前的 `<T>` | 声明方法自己的泛型参数，类似 TS：`function success<T>(data: T): ApiResponse<T>` |
| `new ApiResponse<>(...)` | `<>` 让 Java 推断泛型类型 |

`status: 0` 是项目约定的业务成功码；HTTP 200 是协议层的成功状态，两者不同。

## 7. `final` 与构造器

```java
private final WelcomeService welcomeService;

public WelcomeController(WelcomeService welcomeService) {
    this.welcomeService = welcomeService;
}
```

- Java 构造器名称与类名相同，且没有返回类型；作用类似 TS 的 `constructor`。
- `final` 变量只能赋值一次。
- 对引用类型而言，`final` 表示不能指向另一个对象，不等于该对象内部永远不能变。
- Spring 创建 `WelcomeService` 实例，再通过构造器传给 `WelcomeController`，这叫构造器注入。

## 8. 静态页面为什么不用 Controller

```text
src/main/resources/static/index.html
```

Spring Boot 默认提供 `static` 目录中的静态资源：

```text
static/index.html  →  http://localhost:8080/
static/index.html  →  http://localhost:8080/index.html
```

静态 HTML、CSS、JS、图片不需要 Controller；动态 JSON API 才通常需要 Controller。

## 9. 当前接口清单

| 方法与路径 | 对应知识点 |
| --- | --- |
| `GET /api/hello` | 公共响应、Controller 调用 Service |
| `GET /api/list` | `List<PostItem>` 返回 JSON 数组 |
| `GET /api/demo/query` | `@RequestParam` |
| `GET /api/demo/files/{id}` | `@PathVariable` |
| `POST /api/demo/folders` | `@RequestBody` + DTO |
| `GET /api/demo/header` | `@RequestHeader` |
| `GET /` | 静态 API 测试页面 |

## 10. 每次修改后的检查

```bash
# 编译、测试、打包
mvn clean verify

# 只跑测试
mvn test

# 使用 api.http 在 IDE 中逐条请求接口
```

`target/` 是 Maven 自动生成的构建目录，可以删除后重新构建；不要手动修改其中的 `.class` 或 JAR。

## 11. 下一次练习

实现 `folder` 模块的第一个接口：

```text
POST /api/folders
```

请求 JSON：

```json
{
  "name": "照片",
  "parentId": 0
}
```

先只在内存中返回创建结果；之后再学习校验、异常处理、数据库与真正的文件存储。
