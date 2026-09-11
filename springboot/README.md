# CloudDrive 网盘代理服务

一个用于学习 Spring Boot 和 React 的本地网盘管理项目。后端代理夸克网盘的非官方 Web 接口，前端提供文件、分享和扫码登录的管理页面。

> 这是学习项目，不是夸克官方 SDK。上游接口和字段可能随网页更新而变化；请只使用自己的账号，并妥善保护 Cookie。

## 项目结构

```text
java-study/
├── springboot/                         Spring Boot 2.7 后端
│   ├── src/main/java/com/clouddrive/
│   │   ├── common/                     通用响应对象
│   │   ├── example/                    简单的 /api/list 示例
│   │   └── quark/                      夸克代理：认证、文件、分享
│   └── src/main/resources/static/      前端构建产物
├── frontend/                           React + Vite 前端
├── firstPakage/                        Java / JDBC 早期学习代码
└── learning-log/                       学习笔记
```

## 环境要求

- JDK 8 或更高版本（项目当前使用 Java 8 编译）
- Maven 3.6+
- Node.js 18+ 和 npm

## 快速开始

### 1. 启动后端

在 `springboot` 目录中执行：

**PowerShell：**

```powershell
$env:QUARK_COOKIE = '你的夸克 Cookie' # 可选；也可在页面中填写
mvn spring-boot:run
```

**macOS / Linux：**

```bash
export QUARK_COOKIE='你的夸克 Cookie' # 可选；也可在页面中填写
mvn spring-boot:run
```

启动后访问 <http://localhost:8080/>。如果已经构建过前端，Spring Boot 会直接提供管理页面。

### 2. 前端开发

另开一个终端，在 `frontend` 目录中执行：

```bash
npm ci
npm run dev
```

打开 <http://localhost:5173/>。Vite 会将 `/api` 请求代理到本地后端 `http://127.0.0.1:8080`。

### 3. 构建前端并由后端托管

在 `frontend` 目录执行：

```bash
npm run build
```

构建产物会写入 `../springboot/src/main/resources/static`。之后只需启动 Spring Boot，并访问 <http://localhost:8080/>。

## 测试与打包

在 `springboot` 目录执行：

```bash
mvn test
mvn package
```

测试覆盖示例列表接口，以及文件、分享和扫码认证代理的关键请求转发逻辑。夸克上游请求在测试中通过 Mock 隔离，不会访问真实账号。

## API 概览

所有夸克代理接口均以 `/api/quark` 开头。文件与分享接口会尽量保持上游 JSON 响应体，以方便前端使用真实字段。

| 方法 | 路径 | 用途 |
| --- | --- | --- |
| GET | `/api/list` | 返回固定示例帖子列表 |
| GET | `/api/quark/files` | 文件列表 |
| GET | `/api/quark/files/{fid}` | 文件详情 |
| POST | `/api/quark/files/folders` | 创建文件夹 |
| POST | `/api/quark/files/delete` | 删除文件 |
| POST | `/api/quark/files/rename` | 重命名文件 |
| GET | `/api/quark/files/search` | 搜索文件 |
| GET | `/api/quark/files/tree` | 获取目录树 |
| POST | `/api/quark/files/move` | 移动文件 |
| POST | `/api/quark/files/download` | 获取下载直链 |
| POST | `/api/quark/shares` | 创建分享 |
| GET | `/api/quark/shares` | 获取我的分享列表 |
| DELETE | `/api/quark/shares` | 删除分享 |
| POST | `/api/quark/share-page/token` | 获取分享页 Token |
| GET | `/api/quark/share-page/files` | 浏览分享内容 |
| POST | `/api/quark/share-page/save` | 转存分享内容 |
| GET | `/api/quark/auth/qrcode` | 获取扫码二维码 |
| GET | `/api/quark/auth/qrcode/status` | 轮询扫码状态 |
| GET | `/api/quark/auth/session` | 用 service ticket 换取登录 Cookie |

需要登录态的请求可携带：

```http
X-Quark-Cookie: 你的夸克 Cookie
```

未携带时，后端会回退使用环境变量 `QUARK_COOKIE`。扫码登录成功后，后端会通过 `X-Quark-Cookie` 响应头返回已规范化的 `name=value` Cookie 片段，前端可安全地填入页面输入框。

## 安全说明

- **不要**把真实 Cookie 写入 `application.yml`、JavaScript、README、截图或 Git 提交。
- 前端将手动填写的 Cookie 保存到当前浏览器的 `localStorage`，仅用于向本机后端发送请求；使用完请点击页面的“清除”按钮。
- 代理接口会携带账号权限；不要将本地服务暴露到公网。
- 文件上传流程尚未实现，避免在未完整验证上游分片协议前执行可能产生副作用的操作。
