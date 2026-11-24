package com.java110.ext;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 扩展业务模块启动类
 */
@SpringBootApplication(scanBasePackages = {"com.java110.ext", "com.java110"})
@MapperScan("com.java110.ext.mapper")
@EnableScheduling
@EnableDiscoveryClient
public class ExtServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExtServiceApplication.class, args);
    }
}
