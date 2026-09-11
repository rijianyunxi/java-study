package com.clouddrive.javaBaeStudy.exceptiongeneric;

import java.io.IOException;

/** 直接运行 main：示例不会访问网络或读写真实文件。 */
public class ExceptionDemo {
    public static void main(String[] args) {
        System.out.println("=== 1. try / catch / finally，整体与 TS 类似 ===");
        try {
            checkAge(-1);
            System.out.println("抛异常后，这一行不会执行");
        } catch (IllegalArgumentException ex) {
            // Java 按异常类型匹配 catch；TS 通常在一个 catch 中使用 instanceof 判断。
            System.out.println("捕获参数错误：" + ex.getMessage());
        } finally {
            // 正常完成、抛异常、return 时通常都会执行；不要在这里 return。
            System.out.println("finally：完成本次检查");
        }

        System.out.println("=== 2. throws：声明可能抛出受检异常 ===");
        try {
            enroll("");
        } catch (EnrollmentException ex) {
            System.out.println("捕获报名错误：" + ex.getMessage());
        }
        // enroll(""); // 取消注释会编译失败：必须 catch，或者在当前方法声明 throws。

        System.out.println("=== 3. 成功路径 ===");
        try {
            checkAge(18);
            enroll("小明");
        } catch (EnrollmentException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("=== 4. try-with-resources：自动关闭资源 ===");
        try (DemoResource resource = new DemoResource()) {
            resource.read();
        } catch (IOException ex) {
            System.out.println("资源错误：" + ex.getMessage());
        }
    }

    // RuntimeException 的子类：非受检异常，调用方不必 catch 或声明 throws。
    private static void checkAge(int age) {
        if (age < 0) {
            // throw 真正抛异常；Java 必须抛 Throwable 对象，不能像 JS 那样 throw "错误"。
            throw new IllegalArgumentException("年龄不能小于 0");
        }
    }

    // throws 只声明，不会自动抛异常，也不是返回类型。
    // TS 没有对应的受检异常声明机制；函数签名不会强制调用方处理抛出的异常。
    private static void enroll(String name) throws EnrollmentException {
        if (name == null || name.trim().isEmpty()) {
            throw new EnrollmentException("报名姓名不能为空");
        }
        System.out.println(name + "报名成功");
    }

    // 继承 Exception（而不是 RuntimeException）：自定义受检异常。
    private static class EnrollmentException extends Exception {
        EnrollmentException(String message) {
            super(message);
        }
    }

    private static class DemoResource implements AutoCloseable {
        void read() throws IOException {
            System.out.println("模拟读取资源");
            throw new IOException("模拟读取失败");
        }

        @Override
        public void close() {
            // read 抛异常后，仍先自动调用 close，再进入外面的 catch。
            System.out.println("close：资源已自动关闭");
        }
    }
}
