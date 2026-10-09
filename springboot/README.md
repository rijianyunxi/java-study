# Study Spring Boot 学习项目

Spring Boot 2.7 / Java 8 / MyBatis-Plus 学习示例。
保留通用响应、Java 基础、HTTP、Spring 注解、帖子和用户模块。

## 目录

```text
src/main/java/com/study/
├── StudyApplication.java  启动类（扫描 com.study.mapper）
├── common/                ApiResponse 等公共代码
├── javaBaeStudy/          Java 基础、HTTP 和 Spring 复习示例
├── example/               帖子示例
├── mapper/                MyBatis-Plus Mapper 接口
└── user/                  用户列表、详情、新增、修改、状态修改
```

包名已经迁移到 `com.study`，Maven 坐标为 `com.study:study-server`。
IDEA 重新加载 Maven 项目后运行 `com.study.StudyApplication`，或使用项目的 `.run/StudyApplication.run.xml`。
旧启动类的运行配置不再适用，请移除旧配置或选择新的 StudyApplication。

## 启动

在 springboot 项目根目录执行：

```powershell
mvn spring-boot:run
```

开发环境地址为 `http://127.0.0.1:3000`，生产端口为 9000。
项目不再提供前端首页，请通过 HTTP 客户端访问 API。

## MySQL 配置

开发配置在 `src/main/resources/application-dev.yml`，数据库为 `study`。
密码读取 `DB_PASSWORD` 环境变量或 Git 忽略的 `.local/application-dev.yml`。
必须从 springboot 根目录启动，才能加载这个本地配置文件；不要提交真实密码。

用户接口依赖 `user` 表中的 `id/name/age/email/status`。
如果缺少 status，请先执行 `docs/sql/add-user-status.sql`（只加字段，不删数据）。
真实数据库登录或字段迁移未成功之前，用户查询/写入无法正常使用。

## 接口与示例

- `GET /api/posts/list`：原来的固定帖子列表。
- `POST /api/posts/detail`：帖子数据库详情，依赖独立的 posts 表。
- `GET /api/users`：用户列表。
- `GET /api/users/search?name=Jack`：按姓名查询。
- `GET /api/users/1`：用户详情。
- `POST /api/users`：新增用户。
- `PUT /api/users/1`：修改基本资料。
- `PATCH /api/users/1/status`：独立修改状态（1 启用、0 禁用）。
- `/study/*`：Spring Bean、请求参数与校验练习。

用户的完整说明见 `src/main/java/com/study/user/README.md`。
打开 `user-demo.http` 和 `api.http` 可以逐条请求，不需要前端。
示例没有登录鉴权，只适合本地学习。

## 测试与打包

```powershell
mvn clean verify
mvn -Dtest=UserControllerIntegrationTest test
```

用户集成测试通过 H2 MySQL 模式验证完整查询/写入链路，不改动本机 MySQL 数据。
帖子列表的测试不执行数据库写操作。
