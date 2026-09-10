# CloudDrive React 页面

这是 CloudDrive 网盘代理的 React + Vite 前端。

## 开发

```bash
cd /Users/song/study/java-study/frontend
npm install
npm run dev
```

打开 `http://localhost:5173/`。Vite 会把 `/api` 请求代理到本地 Spring Boot `8080` 端口。

## 构建

```bash
npm run build
```

生产构建会输出到 `../springboot/src/main/resources/static`，由 Spring Boot 直接提供：`http://localhost:8080/`。

## Cookie

页面顶部可以输入夸克 Cookie。Cookie 只在浏览器本地保存，并通过 `X-Quark-Cookie` 请求头发送给本机 Java 代理；不要把真实 Cookie 提交到 Git。
