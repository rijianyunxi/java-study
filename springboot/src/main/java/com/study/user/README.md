# User 表完整示例

调用链：UserController → UserService → UserMapper（BaseMapper）→ MySQL study.user。
本模块不改动帖子接口；新增参数使用 DTO，数据库映射使用 Entity。

## 先加数据库字段

当前表有 id/name/age/email，运行新版前必须加 status：

```sql
SHOW COLUMNS FROM study.`user` LIKE 'status';
-- 如果没有返回列信息，再执行；不要重复加列，也不要删除表。
ALTER TABLE study.`user`
  ADD COLUMN `status` TINYINT NOT NULL DEFAULT 1 COMMENT '用户状态：1启用，0禁用';
```

脚本在 `springboot/docs/sql/add-user-status.sql`。它不由 Spring 自动执行。
已有五条用户数据会得到默认状态 1，不会删除或重建表。

## 接口

| 方法 | 路径 | 功能 |
| --- | --- | --- |
| GET | /api/users | 列表 |
| GET | /api/users/search?name=Jack | 按姓名精确查询 |
| GET | /api/users/1 | 详情 |
| POST | /api/users | 新增：id/name/age/email；默认启用 |
| PUT | /api/users/1 | 修改基本资料：name/age/email 必传，不修改状态 |
| PATCH | /api/users/1/status | 单独修改状态：只传 status |

打开项目根目录 `user-demo.http` 可以逐条执行。
数据库 ID 非自增（IdType.INPUT），新增必须指定一个不存在的正整数 ID。
状态只有 1（启用）和 0（禁用）；写操作不改原有五条数据，除非你主动发送修改请求。
新增返回 201，普通成功返回 200，非法请求返回 400，用户不存在返回 404，ID 重复返回 409。
错误继续使用统一响应格式：status 为错误码，result=false，msg 为错误提示，data=null。

## Entity / DTO

- User：映射数据库的全部列。
- CreateUserRequest：新增可输入的字段，状态由服务端默认启用。
- UpdateUserRequest：基本资料可修改的字段，不接收 ID/状态。
- UpdateUserStatusRequest：只接受状态。

基本资料和状态的 SQL 分别更新各自的列，避免读整个 Entity 后再写回导致意外覆盖其他字段。
没有增加登录鉴权，本模块仅用于本地学习，不能直接当作公开的生产接口。

## 本地配置与测试

生产代码仍连接 MySQL；密码位于项目根目录的 `.local/application-dev.yml`（被 Git 忽略），也可用 DB_PASSWORD 环境变量。
`.local` 配置按当前工作目录查找，从 springboot 项目根目录启动应用。
本地文件通过 `${DB_PASSWORD:本地密码}` 提供默认值，因此 DB_PASSWORD 环境变量可覆盖本地密码。

测试只使用 H2 MySQL 模式，SQL 在 src/test/resources 下，不会修改本机数据库。
运行：`mvn -Dtest=UserControllerIntegrationTest test`。
覆盖：列表、详情、创建、重复 ID、资料修改、状态修改、重复同值修改、不存在用户、参数校验及坏 JSON。
