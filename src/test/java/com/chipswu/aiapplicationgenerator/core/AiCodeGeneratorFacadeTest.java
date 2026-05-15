package com.chipswu.aiapplicationgenerator.core;

import com.chipswu.aiapplicationgenerator.modal.enums.CodeGenTypeEnum;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

import java.io.File;
import java.util.List;

/**
 * 代码生成器测试类
 *
 * @author WuJiaJun
 */
@SpringBootTest
class AiCodeGeneratorFacadeTest {

    @Resource
    private AiCodeGeneratorFacade aiCodeGeneratorFacade;

    @Test
    void generateAndSaveCode() {
        File file = aiCodeGeneratorFacade.generateAndSaveCode("生成一个注册页面", CodeGenTypeEnum.MULTI_FILE);
        Assertions.assertNotNull(file);
    }


    @Test
    void generateAndSaveCodeStream() {
        Flux<String> codeStream = aiCodeGeneratorFacade.generateAndSaveCodeStream("生成一个注册页面", CodeGenTypeEnum.MULTI_FILE);
        // 阻塞等待所有数据收集完毕
        List<String> result = codeStream.collectList().block();
        Assertions.assertNotNull(result);
        // 拼接列表，得到完整内容
        String completeContent = String.join("", result);
        Assertions.assertNotNull(completeContent);
    }
}