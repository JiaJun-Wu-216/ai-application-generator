package com.chipswu.aiapplicationgenerator;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.ConfigurableEnvironment;

@MapperScan("com.chipswu.aiapplicationgenerator.mapper")
@Slf4j
@SpringBootApplication
public class AiApplicationGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(AiApplicationGeneratorApplication.class);
        ConfigurableEnvironment environment = application.run(args).getEnvironment();
        log.info("启动成功......");
        log.info("地址：\thttp://127.0.0.1:{}{}",
                environment.getProperty("server.port"),
                environment.getProperty("server.servlet.context-path"));
    }

}
