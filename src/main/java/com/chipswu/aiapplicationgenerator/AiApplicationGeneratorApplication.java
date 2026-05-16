package com.chipswu.aiapplicationgenerator;

import dev.langchain4j.community.store.embedding.redis.spring.RedisEmbeddingStoreAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.ConfigurableEnvironment;

@MapperScan("com.chipswu.aiapplicationgenerator.mapper")
@Slf4j
@SpringBootApplication(exclude = {RedisEmbeddingStoreAutoConfiguration.class})  // 排除 LangChain4j 中的 Redis 向量存储自动配置
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
