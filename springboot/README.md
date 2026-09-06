# CloudDrive 网盘后端服务

CloudDrive 是计划用于网盘业务的后端项目，目前完成基础框架和示例接口，尚未实现文件上传、下载、目录管理或用户认证。

## 项目标识

| 项目 | 当前名称 |
| --- | --- |
| 项目名称 | CloudDrive（网盘后端服务） |
| Maven groupId / Java 基础包 | `com.clouddrive` |
| Maven artifactId / 应用名称 | `cloud-drive-server` |
| 启动类 | `com.clouddrive.CloudDriveApplication` |
| 打包文件 | `target/cloud-drive-server-0.0.1-SNAPSHOT.jar` |

项目已迁移到 `~/study/java-study/springboot`（即 `/Users/song/study/java-study/springboot`）。在 IDE 中打开这个目录并导入 `pom.xml`；启动时选择 `CloudDriveApplication`。
本次只调整项目和代码命名，保留 `GET /api/hello` 的 `hello world` 响应及 `GET /api/list` 的固定帖子列表，作为兼容的学习示例，不将它们伪装成已经实现的网盘功能。

以下文档配合当前项目阅读，适合只掌握 Java 基础语法、还没学过 Spring 的同学。

**第一个目标：启动程序，在浏览器中拿到一段 JSON，并理解这段 JSON 是怎么产生的。**

## 1. 先运行起来

### 环境

本项目固定使用：

- Java 8（项目创建时本机已安装 Java 8）。
- Maven（项目创建时本机已安装 Maven 3.9.16）。
- Spring Boot 2.7.18。
- Spring MVC + 内嵌 Tomcat；暂不使用数据库、Redis、登录认证或 Lombok。

> 版本提醒：Spring Boot 2.x 是旧版本系列，本项目按你的要求用于学习，不建议直接作为新生产项目的基础。部署真实项目之前，应重新核对 Spring 官方支持周期和安全公告，并选择受支持的版本。

在终端执行：

```bash
cd ~/study/java-study/springboot
java -version
mvn -version
mvn spring-boot:run
```

首次启动时 Maven 可能需要联网下载依赖。看到 `Started CloudDriveApplication` 后，保持终端运行。

在浏览器地址栏输入：`http://localhost:8080/api/hello`。

也可以另开一个终端执行：

```bash
curl -i http://localhost:8080/api/hello
```

预期 HTTP 状态码为 **200**，响应体为：

```json
{
  "status": 0,
  "result": true,
  "msg": "",
  "data": {
    "text": "hello world"
  }
}
```

注意：

- 你最初写的 `data::` 在这里修正为 `data:`。
- JSON 的字段名和字符串使用双引号，布尔值 `true` 不加引号。
- `msg` 表示响应提示信息，成功时返回空字符串。
- `status: 0` 是我们自定义的业务成功标记，**不是 HTTP 状态码**。
- JSON 对象字段的排列顺序不影响含义。
- 浏览器展示一行紧凑 JSON 也是正常的。
- 当前访问根路径 `/` 会展示 `static/index.html` 提供的接口测试页；`/api/hello` 和 `/api/list` 是后端 JSON 接口。

停止程序：在启动它的终端中按 `Ctrl+C`。

### 用 IDE 启动

使用你现有的 Java IDE 打开当前目录，将 `pom.xml` 导入为 Maven 项目，选择 JDK 8，等待依赖导入完成。然后运行 `CloudDriveApplication` 的 `main` 方法。不需要额外安装或部署 Tomcat。

## 2. 你正在学习的几个概念

| 名称 | 先这样理解 | 本项目中的体现 |
| --- | --- | --- |
| Maven | 管理依赖、编译、测试和打包的工具 | `pom.xml` 描述项目需要哪些依赖 |
| HTTP | 浏览器和服务器通信的一套规则 | 浏览器发送 GET 请求，应用返回响应 |
| Web 接口 | 通过“请求方法 + 路径”访问的一项能力 | `GET /api/hello` |
| JSON | 用来交换数据的文本格式 | 上面的响应体 |
| Spring | 帮你管理对象及对象之间依赖的框架 | 创建 Controller 和 Service，并连接它们 |
| Spring MVC | Spring 中处理 Web 请求的一套机制 | 把 `/api/hello` 路由到 Java 方法 |
| Spring Boot | 简化 Spring 应用配置、依赖组合和启动 | 一个 `main` 方法启动整个应用 |
| Tomcat | 接收 HTTP 请求的 Web 服务器 | 随 Web starter 引入，在应用中启动 |

这里的“接口”是 **HTTP API**，不是 Java 的 `interface` 关键字。Controller 和 Service 都是普通 Java 类。

## 3. 目录结构：每个文件负责什么

```text
springboot/
├── pom.xml
├── README.md
├── api.http
├── .gitignore
├── src/
│   ├── main/
│   │   ├── java/com/clouddrive/
│   │   │   ├── CloudDriveApplication.java       启动入口
│   │   │   ├── common/                         跨业务公共代码
│   │   │   │   └── ApiResponse.java
│   │   │   ├── welcome/                        欢迎示例模块
│   │   │   │   ├── controller/WelcomeController.java
│   │   │   │   ├── service/WelcomeService.java
│   │   │   │   └── dto/WelcomeData.java
│   │   │   └── post/                           帖子模块
│   │   │       ├── controller/PostController.java
│   │   │       ├── service/PostService.java
│   │   │       └── dto/PostItem.java
│   │   └── resources/application.yml
│   └── test/
│       ├── java/com/clouddrive/
│       │   ├── welcome/controller/WelcomeControllerTest.java
│       │   └── post/controller/PostControllerTest.java
│       └── resources/posts-response.json
└── target/                         Maven 构建产物，不要手动修改
```

这些源码目录都位于当前项目目录下。现在采用**先按业务模块分包，再在模块内按职责分层**的方式：

- `welcome`：原有欢迎接口 `GET /api/hello`。
- `post`：固定帖子列表接口 `GET /api/list`。
- `common`：两个模块共享的 `ApiResponse`，不在每个模块中复制一份。
- 模块内的 `controller` 接收请求，`service` 处理业务，`dto` 承载数据。这些目录名是组织约定，不是 Java 的特殊语法。
- 测试代码也按对应业务模块组织；完整响应测试数据仍放在测试资源目录。

例如，帖子 Controller 的包声明为 `package com.clouddrive.post.controller;`，它引用的 Service 位于 `com.clouddrive.post.service`。
启动类仍在基础包 `com.clouddrive` 下，两个模块都是其子包，不需要额外配置扫描，也不需要增加启动类。
**包结构调整不改变 URL。** `/api/list` 仍由 `@RequestMapping("/api")` 和 `@GetMapping("/list")` 决定，不会自动变成 `/post/api/list`。

后续新增上传、下载等功能时，可以按需增加 `file`、`folder`、`user`、`share` 模块；目前不预建空目录，也不把帖子示例当作真实的网盘文件模块。

没有数据库，因此暂时不建立 Repository/Mapper 层。对于 Hello World，Service 不是技术上的必需品；保留它，是为了让你先认识简单的分层方式。目前只有一个实现，不必额外创建 Service 接口和实现类。

## 4. 一次请求是如何走完的

```text
浏览器 / curl
    │ GET /api/hello
    ▼
内嵌 Tomcat 接收请求 → Spring MVC 根据路径找到方法
    ▼
WelcomeController.welcome()
    ▼
WelcomeService.getWelcomeData()
    ▼
new WelcomeData("hello world")
    ▼
ApiResponse.success(data) 包装结果
    ▼
JSON 转换器（Jackson）将 Java 对象转成 JSON
    ▼
客户端收到 HTTP 200 和 JSON 响应体
```

你不需要自己拼接 JSON 字符串，也不需要自己操作网络 Socket。

### 4.1 启动类

```java
@SpringBootApplication
public class CloudDriveApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloudDriveApplication.class, args);
    }
}
```

先把 `@SpringBootApplication` 理解为“这是 Spring Boot 的启动配置”。它组合了配置、自动配置和组件扫描等能力。

**启动类放在 `com.clouddrive` 包下，其他业务类放在它的子包中。** 这样默认组件扫描就能找到它们。初学时不要随意把 Controller 移到另一个不相关的包，否则可能访问不到接口。

### 4.2 Controller：将 URL 映射到方法

```java
@RestController
@RequestMapping("/api")
public class WelcomeController {
    private final WelcomeService welcomeService;

    public WelcomeController(WelcomeService welcomeService) {
        this.welcomeService = welcomeService;
    }

    @GetMapping("/hello")
    public ApiResponse<WelcomeData> welcome() {
        return ApiResponse.success(welcomeService.getWelcomeData());
    }
}
```

- `@RestController`：声明处理 Web 请求的组件，方法返回值写入响应体，而不是作为页面名称。
- `@RequestMapping("/api")`：这个类中接口的公共路径前缀。
- `@GetMapping("/hello")`：将 GET 请求映射到 `welcome()` 方法。
- 类路径和方法路径组合后，完整路径就是 `/api/hello`。
- `welcome()` 是 Java 方法名，不决定 URL；URL 由注解决定。
- 在本项目的 Web 配置下，返回对象会由 Jackson 序列化为 JSON。

### 4.3 Service 与依赖注入

```java
@Service
public class WelcomeService {
    public WelcomeData getWelcomeData() {
        return new WelcomeData("hello world");
    }
}
```

`@Service` 让 Spring 发现并管理这个业务对象。

以前你可能这样使用对象：

```java
WelcomeService service = new WelcomeService();
```

这个项目中，Spring 创建 `WelcomeService`，再通过 `WelcomeController` 的构造器将它传入。这叫**构造器依赖注入（DI）**。由框架负责对象的创建和装配，是理解**控制反转（IoC）**的一个具体入口。

Controller 只有一个构造器，因此这里不用额外写 `@Autowired`。`final` 表示这个依赖字段在初始化后不能重新赋值。

不是所有对象都必须交给 Spring：`WelcomeData` 这种用于承载一次响应的数据对象，仍然可以直接 `new`。

### 4.4 返回对象、泛型与 getter

`ApiResponse<T>` 中的 `T` 是泛型类型参数，表示 `data` 中放什么类型的数据。

本接口返回 `ApiResponse<WelcomeData>`，所以 `data` 是 `WelcomeData` 对象，而不是字符串。

```java
return ApiResponse.success(new WelcomeData("hello world"));
```

这个工厂方法统一填入 `status=0`、`result=true`、`msg=""`。后续接口也可以复用它。

`getStatus()`、`isResult()`、`getMsg()`、`getData()` 和 `getText()` 是 getter，Jackson 在当前配置下通过它们读取属性并生成 JSON。布尔值 getter 常用 `isXxx()` 命名。

这些响应类只有构造方法和 getter，没有 setter：创建后不需要修改，适合当前只负责**输出响应**的场景。以后接收 POST 请求时，再单独学习请求 DTO 和 JSON 反序列化，不要直接照搬这里的构造方式。

**当前只统一了这个接口的正常响应，没有实现全局异常处理。** 404、405 或框架异常不保证使用 `ApiResponse` 的结构。

### 4.5 配置文件

`application.yml` 中：

```yaml
spring:
  application:
    name: cloud-drive-server

server:
  address: 127.0.0.1
  port: 8080
```

- `spring.application.name` 是应用名称，不是 URL 路径前缀。
- `server.port` 是监听端口。
- `server.address` 让学习服务只监听本机；其他设备不能直接访问。
- YAML 使用空格缩进，不要用 Tab。
- 修改 Java 代码或配置后，本项目需要手动重启；没有配置热更新。

## 5. 测试、打包、运行

以下命令均在项目根目录执行。

### 运行自动化测试

```bash
mvn test
```

`WelcomeControllerTest` 使用 JUnit 5、Spring Boot 测试上下文和 MockMvc，验证：

1. `GET /api/hello` 返回 HTTP 200。
2. Content-Type 是 JSON 类型。
3. 响应的全部字段和值与约定一致，不允许额外字段。

MockMvc 通过测试框架模拟 HTTP 请求，不需要你事先手动启动服务，也不会占用 8080 端口。它不等同于通过真实网络访问；真实启动后还可以用前面的 curl 命令检查。

### 完整检查并打包

```bash
mvn clean verify
```

这会清理旧构建产物、编译、运行测试并打包。看到 `BUILD SUCCESS` 表示本次 Maven 构建成功。

然后可以不通过 IDE，直接启动生成的 JAR：

```bash
java -jar target/cloud-drive-server-0.0.1-SNAPSHOT.jar
```

不要在已有服务占用 8080 时再次启动同端口的服务。两种启动方式任选一种即可。

临时更换端口而不修改配置文件：

```bash
java -jar target/cloud-drive-server-0.0.1-SNAPSHOT.jar --server.port=8081
```

对应访问地址为 `http://localhost:8081/api/hello`。

## 6. 学习路线：每一步都有可验证的结果

不需要先读完整个 Spring 源码，也不用一开始就学微服务。按下面顺序，在这个项目上逐步增加能力。

### 第一步：补齐用得上的 Java 与 Web 基础

- Java：类与对象、构造方法、访问修饰符、接口、泛型、集合、异常、注解的基本作用。
- 工具：理解 Maven 的 `groupId`、`artifactId`、`version`、依赖和生命周期。
- Web：理解 URL、端口、GET/POST、请求参数、请求头、请求体、HTTP 状态码、JSON。
- 完成标准：能解释 `ApiResponse<WelcomeData>`，并区分 HTTP 200 与业务 `status: 0`。

### 第二步：看懂并修改当前接口

- 按“启动类 → Controller → Service → DTO → 统一响应”的顺序读代码。
- 把 `hello world` 改成 `你好，Spring Boot`，重启后检查响应。
- 对应修改测试期望，再运行 `mvn test`。
- 完成标准：能自己定位要改的类，不只会复制粘贴。

### 第三步：接收参数

- 学习 `@RequestParam`，让接口能接收 `?name=小明`。
- 学习 `@PathVariable`，理解 `/api/users/1` 中的路径变量。
- 建议新增接口练习，保留当前 `/api/hello` 的契约。
- 完成标准：能够区分查询参数和路径变量，并处理缺少参数的情况。

### 第四步：POST、请求 DTO 与校验

- 学习 `@PostMapping`、`@RequestBody`，通过 JSON 请求体传递数据。
- 为输入创建独立的请求 DTO，不把输入与输出混在一起。
- 然后引入校验依赖，学习 `@Valid` 与常见约束。
- 注意 2.x 教程常使用 `javax.*`；不要不加区分地复制使用 `jakarta.*` 的新主版本教程。
- 完成标准：实现一个接收 JSON 的接口，并能清楚返回参数错误。

### 第五步：统一异常处理

- 学习 `@RestControllerAdvice`、`@ExceptionHandler`。
- 定义失败返回结构与业务错误码，决定不同失败场景的 HTTP 状态码。
- 不向客户端暴露异常堆栈或敏感配置。
- 完成标准：正常和错误场景都有测试，而不是只测“能成功”。

### 第六步：最后再接数据库

- 先学习关系表、主键和基本 SQL。
- 然后选择一种数据访问方式，例如 MyBatis 或 Spring Data JPA，不要一开始两种都学。
- 增加持久化层，完成一个简单的增删改查练习。
- 再学事务、分页、配置环境区分与凭据管理。
- 完成标准：能解释 Controller、Service、持久化层分别负责什么。

学完这些，再考虑登录、安全、缓存、部署和版本迁移。当前阶段不必引入 Spring Cloud、消息队列或复杂架构。

## 7. 常见问题

| 现象 | 优先检查 |
| --- | --- |
| `mvn: command not found` | Maven 是否安装，终端 PATH 是否正确 |
| 下载依赖失败 | 网络、Maven 仓库或代理设置；不要用关闭 TLS 校验的方式解决 |
| 提示端口被占用 | 是否已经在其他终端或 IDE 启动过；停止旧进程或换 8081 |
| 浏览器提示连接失败 | 应用是否仍在运行、启动日志是否报错、端口是否一致 |
| 返回 404 | 是否访问 `/api/hello`，Controller 是否在启动类所在包或子包中 |
| POST 返回 405 | 当前接口仅支持 GET，改用 GET 请求 |
| 修改后响应没变化 | 是否重启了应用；使用 JAR 时是否先重新打包 |
| 测试因为文本不一致失败 | 是否修改了 Service 返回文本但没有同步测试期望 |
| IDE 无法识别 Spring 注解 | 是否以 Maven 项目导入、依赖是否下载完成、是否需要刷新 Maven |

## 8. 下一次练习

**尝试新增 `GET /api/greet?name=小明`，让 `data.text` 返回 `hello 小明`。**

先自己想清楚：

1. 请求参数应该在哪一层接收？
2. 拼接问候语的业务逻辑应该放在哪一层？
3. 参数没传时要默认返回什么？
4. 需要添加哪些测试来证明它正确？

保留当前欢迎接口和测试，可以用它们确认后续修改没有破坏原有功能。


## 9. 新增的帖子列表接口

启动后访问 `http://localhost:8080/api/list`，或执行：

```bash
curl http://localhost:8080/api/list
```

该接口复用 `ApiResponse<T>`，返回 `status: 0`、`result: true`、`msg: ""`，以及数组类型的 `data`。
数组包含提供的全部 18 条帖子，每个对象只有 `id`（数字）和 `title`（字符串）。
`id` 取链接中的帖子编号，不是 01～18 的显示序号；标题和顺序保持原样，包括截断的标题与 emoji。
这是固定示例数据，不会抓取链接页面，也不会自动更新。

### 新增代码的分工

- `PostController`：通过 `@GetMapping("/list")` 提供列表接口。
- `PostService`：在 `listPosts()` 中构建 18 条固定数据。
- `PostItem`：表示数组中的一个对象，包含 `id`、`title` 及其 getter。
- `PostControllerTest`：验证 HTTP 200、JSON 类型、数组长度，以及全部字段、内容和顺序。
- `src/test/resources/posts-response.json`：测试使用的完整预期响应，不是正式服务的数据源。

正式代码位于 `com.clouddrive.post` 下对应的 controller、service、dto 子包中，接口测试位于测试源码的 `com.clouddrive.post.controller` 包中。
这里按“帖子”业务模块新增类，而不是规定每个接口都要新建一套类。后续帖子详情接口可以继续放在 `PostController` 中。
欢迎示例由 `WelcomeController`、`WelcomeService`、`WelcomeData` 负责；帖子模块与它分开，公共 `ApiResponse` 不需要修改。

关键返回类型：

```java
public ApiResponse<List<PostItem>> list() {
    return ApiResponse.success(postService.listPosts());
}
```

由内向外理解：

1. `PostItem` 是一条帖子，例如 `{"id":1578941,"title":"没有灵根真的不能修仙"}`。
2. `List<PostItem>` 是多条帖子组成的 Java 列表，序列化后就是 JSON 数组。
3. `ApiResponse<List<PostItem>>` 表示公共响应中的 `data` 装的是这个列表。

因此 `data` 直接是 `[...]`，不是字符串，也不是额外套一层 `{"list": [...]}`。
运行 `mvn test` 会同时检查原有 hello 接口与新增列表接口。

## 10. Spring Boot 传参与参数解析演示

新增的学习模块位于 `com.clouddrive.demo`：

```text
src/main/java/com/clouddrive/demo/
├── controller/ParameterDemoController.java
└── dto/CreateFolderRequest.java
```

这里没有创建 Service，因为全部接口只负责“接收请求并原样展示 Spring 解析到的值”，没有真实业务逻辑、数据库操作或文件保存。真实网盘功能有业务规则时，再把逻辑放进对应模块的 Service。

在支持 `.http` 文件的 IDE 中直接运行 `api.http` 新增的四个请求；也可以在浏览器中测试前两个 GET 请求。POST 和请求头示例建议用 `.http` 文件或 curl。

### 10.1 总过程：请求文本如何变成 Java 参数

```text
客户端 HTTP 请求
    ↓
Spring 根据 HTTP 方法和 URL 找到 ParameterDemoController 的对应方法
    ↓
Spring MVC 根据参数上的注解，从不同位置读取数据
    ├── @RequestParam  ：URL 的 ?name=小明
    ├── @PathVariable  ：URL 路径的 /files/123
    ├── @RequestBody   ：POST 请求体中的 JSON
    └── @RequestHeader ：请求头中的 X-Client-Name
    ↓
把字符串转换成目标 Java 类型（例如 "123" → Long 123），或把 JSON 转为 DTO 对象
    ↓
调用 Controller 方法
    ↓
返回 Java 对象，再由 Jackson 序列化为 JSON
```

### 10.2 `@RequestParam`：解析查询参数

请求：

```text
GET /api/demo/query?name=小明&keyword=照片
```

代码：

```java
@GetMapping("/query")
public ApiResponse<Map<String, Object>> query(
        @RequestParam String name,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(required = false) String keyword) {
    // ...
}
```

对应关系：

| 请求中的内容 | Java 参数 | 结果 |
| --- | --- | --- |
| `name=小明` | `String name` | `"小明"` |
| `page` 未传 | `int page` | 默认值 `1` |
| `keyword=照片` | `String keyword` | `"照片"` |

`@RequestParam String name` 默认是必传的。若不传 `name`，Spring 会在调用方法前返回 HTTP 400；当前项目尚未配置统一异常响应，因此该错误不会使用 `ApiResponse` 的结构。

### 10.3 `@PathVariable`：解析路径参数

请求：

```text
GET /api/demo/files/123
```

代码：

```java
@GetMapping("/files/{id}")
public ApiResponse<Map<String, Object>> fileDetail(
        @PathVariable("id") Long fileId) {
    // fileId 的值是 Long 类型的 123
}
```

`{id}` 是路径中的占位符。`@PathVariable("id")` 表示把这个位置的值取出，赋值给 Java 参数 `fileId`；写明 `"id"` 是因为 URL 占位符名与 Java 变量名不同。
如果请求为 `/api/demo/files/abc`，Spring 无法把 `abc` 转成 `Long`，会返回 HTTP 400。

### 10.4 `@RequestBody`：将 JSON 转为 DTO

请求：

```http
POST /api/demo/folders
Content-Type: application/json

{
  "name": "学习资料",
  "parentId": 0
}
```

代码：

```java
@PostMapping("/folders")
public ApiResponse<CreateFolderRequest> createFolder(
        @RequestBody CreateFolderRequest request) {
    return ApiResponse.success(request);
}
```

Spring 使用 Jackson 做两次转换：

```text
请求 JSON
  ↓ 反序列化
CreateFolderRequest 对象
  ↓ 作为方法参数 request
Controller 返回 request
  ↓ 序列化
响应 JSON
```

`CreateFolderRequest` 采用无参构造器和 setter，供 Jackson 写入请求中的字段；getter 则让 Jackson 能在响应时读取字段。这个演示只回显数据，不代表已经创建文件夹。

### 10.5 `@RequestHeader`：读取请求头

请求头：

```text
X-Client-Name: web-client
```

代码：

```java
@RequestHeader(value = "X-Client-Name", defaultValue = "anonymous")
String clientName
```

如果请求携带该头，`clientName` 是 `"web-client"`；没有携带时，`clientName` 是默认值 `"anonymous"`。

### 10.6 为什么演示中使用 `Map<String, Object>`？

`query`、`fileDetail`、`header` 三个学习接口用 `Map` 只是为了直观看到“解析到了哪些参数”。正式 API 的响应结构通常更稳定，建议创建明确的响应 DTO，例如 `FileDetailResponse`，不要长期用任意键值的 `Map` 作为对外契约。

运行：

```bash
mvn test
```

会验证四类参数解析，以及原有欢迎、帖子接口。当前共 6 个测试方法。

## 11. 浏览器接口测试页面

项目内置了不依赖 React、Vue 或额外 npm 安装的静态测试页面：

```text
src/main/resources/static/index.html
```

启动服务后，浏览器访问：

```text
http://localhost:8080/
```

页面由 Spring Boot 直接提供，和 API 同源，因此浏览器可以直接调用 `/api/...` 接口，不需要额外配置 CORS。
它支持调用：

- `GET /api/hello`：欢迎接口。
- `GET /api/list`：帖子列表。
- `GET /api/demo/query`：`@RequestParam` 查询参数。
- `GET /api/demo/files/{id}`：`@PathVariable` 路径参数。
- `POST /api/demo/folders`：`@RequestBody` JSON 请求体。
- `GET /api/demo/header`：`@RequestHeader` 请求头。

页面右侧会显示实际发出的 URL、请求头、JSON 请求体及服务端响应。它是本地学习和调试工具，当前并未实现登录或访问控制，不能直接当作生产管理后台。
