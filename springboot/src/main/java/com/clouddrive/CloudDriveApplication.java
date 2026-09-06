package com.clouddrive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** 应用入口：运行 main 方法即可启动 Web 服务。 */
@SpringBootApplication
public class CloudDriveApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudDriveApplication.class, args);
    }
}
