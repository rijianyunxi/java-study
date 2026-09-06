# 学习日志：Spring Boot 2.x 入门与 CloudDrive 基础框架

- **日期**：2026 年 9 月 6 日
- **项目**：`springboot` 模块（CloudDrive 网盘后端服务）
- **学习环境**：Java 8、Maven 3.9、Spring Boot 2.7.18

## 今天完成了什么

1. 使用 Maven 创建并运行 Spring Boot 2.x Web 项目。
2. 将项目的 Maven 身份和 Java 基础包从示例名称改为网盘项目名称：
   - `groupId`：`com.clouddrive`
   - `artifactId`：`cloud-drive-server`
   - 启动类：`CloudDriveApplication`
3. 建立公共响应类 `ApiResponse<T>`，统一正常响应结构：

   ```json
   {
     "status": 0,
     "result": true,
     "msg": "",
     "data": {}
   }
   ```

4. 从“按技术层分包”调整为“先按业务模块分包，再在模块内按职责分层”。
5. 实现欢迎、帖子列表和参数解析示例接口。
6. 为接口添加 MockMvc 自动化测试，并通过 Maven 构建验证。
7. 增加 `src/main/resources/static/index.html`，在浏览器中直接测试接口与参数解析。
8. 将项目迁移到本仓库的 `springboot/` 目录，保留项目内 README，并增加复习速查文档。

## 当前目录结构

```text
java-study/
├── firstPakage/                     已有 Java 学习项目
├── learning-log/                    按日期记录学习过程
│   └── 2026-09-06-spring-boot-2x.md
└── springboot/                      CloudDrive Spring Boot 项目
    ├── README.md                    较完整的项目与学习说明
    ├── REVIEW.md                    本次复习速查
    ├── api.http                     IDE 可直接执行的接口请求
    ├── pom.xml
    └── src/main/java/com/clouddrive/
        ├── CloudDriveApplication.java
        ├── common/
        ├── welcome/
        ├── post/
        └── demo/
```

## 已实现接口

| 请求 | 用途 | 重点 |
| --- | --- | --- |
| `GET /api/hello` | 欢迎响应 | `@RestController`、统一响应 |
| `GET /api/hello2` | 简单字符串响应 | `@GetMapping` |
| `GET /api/list` | 固定帖子列表 | `List<PostItem>`、JSON 数组 |
| `GET /api/demo/query?name=小明&page=2&keyword=照片` | 查询参数演示 | `@RequestParam` |
| `GET /api/demo/files/123` | 路径参数演示 | `@PathVariable` |
| `POST /api/demo/folders` | JSON 请求体演示 | `@RequestBody`、DTO |
| `GET /api/demo/header` | 请求头演示 | `@RequestHeader` |
| `GET /` | 浏览器测试页面 | `static/index.html` |

## 今天理解的概念

### Spring Boot 的启动

`CloudDriveApplication.main()` 是 Java 程序入口。`SpringApplication.run(...)` 会创建 Spring 容器、扫描组件、创建 Controller/Service，并启动内嵌 Tomcat 监听端口 8080。

### Controller、Service、DTO

- **Controller**：HTTP 请求进入、响应返回的入口；负责路径、请求方法、参数接收和调用业务方法。
- **Service**：处理业务规则；当前帖子示例中只是组装固定数据，真实网盘中会负责权限、容量、存储、数据库等逻辑。
- **DTO**：定义并承载传输的数据。例如 `PostItem` 有 `id`、`title`；`CreateFolderRequest` 接收 JSON 的 `name`、`parentId`。

不是每个接口都必须新建一套 Controller、Service、DTO。先按业务归属放进模块；简单接口可只增加 Controller 方法；需要复杂规则再在对应 Service 新增方法；输入或输出需要明确结构时再建 DTO。

### Spring 的依赖注入

`WelcomeController` 的构造器需要 `WelcomeService`：

```java
public WelcomeController(WelcomeService welcomeService) {
    this.welcomeService = welcomeService;
}
```

Spring 发现 `@Service` 后创建 Service 实例，再创建 `@RestController` 时把该实例传入。业务代码通常不手动 `new WelcomeService()`。

### 泛型与公共响应

```java
public static <T> ApiResponse<T> success(T data)
```

方法前的 `<T>` 是方法自己的类型参数声明；它和 TS 的写法对应：

```ts
function success<T>(data: T): ApiResponse<T>
```

`new ApiResponse<>(...)` 中的 `<>` 是 Java 菱形语法，让编译器从上下文推断具体泛型类型。

### 参数解析

- `@RequestParam`：读取 `?name=小明`。
- `@PathVariable`：读取 `/files/123` 中的 `123`。
- `@RequestBody`：将 JSON 反序列化为 DTO。
- `@RequestHeader`：读取 `X-Client-Name` 等请求头。

### 静态页面

`src/main/resources/static/index.html` 会被 Spring Boot 自动作为静态资源提供。`index.html` 是默认欢迎页，因此访问 `/` 即可看到页面；它不需要 Controller 注册。

## 验证与启动命令

在仓库根目录执行：

```bash
cd springboot
mvn clean verify
mvn spring-boot:run
```

启动后访问：

```text
http://localhost:8080/
```

停止服务：在启动终端按 `Ctrl + C`。

## 下一步建议

1. 将欢迎和帖子示例逐步替换为网盘真实业务。
2. 新增 `file` 模块：文件列表、文件详情、上传、下载、删除。
3. 新增 `folder` 模块：创建、重命名、移动、删除文件夹。
4. 学习 `@Valid`、`@RestControllerAdvice`，让参数校验和异常也使用统一响应格式。
5. 再接入数据库，并在 Service 中调用 Repository 或 Mapper。
6. 在真实上传前学习文件大小限制、文件名安全、权限校验、路径遍历防护和对象存储。
