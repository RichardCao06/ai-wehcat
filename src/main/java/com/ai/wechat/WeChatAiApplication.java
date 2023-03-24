package com.ai.wechat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@MapperScan("com.ai.wechat.mapper")
@SpringBootApplication
@EnableAsync
public class WeChatAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeChatAiApplication.class, args);
    }
}
