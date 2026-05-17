package com.chipswu.aiapplicationgenerator.core;

import com.chipswu.aiapplicationgenerator.modal.enums.CodeGenTypeEnum;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

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
        /*File file = aiCodeGeneratorFacade.generateAndSaveCode("生成一个注册页面", CodeGenTypeEnum.MULTI_FILE);
        Assertions.assertNotNull(file);*/
    }


    @Test
    void generateAndSaveCodeStream() {
        /*Flux<String> codeStream = aiCodeGeneratorFacade.generateAndSaveCodeStream("生成一个注册页面", CodeGenTypeEnum.MULTI_FILE);
        // 阻塞等待所有数据收集完毕
        List<String> result = codeStream.collectList().block();
        Assertions.assertNotNull(result);
        // 拼接列表，得到完整内容
        String completeContent = String.join("", result);
        Assertions.assertNotNull(completeContent);*/
    }

    @Test
    void generateVueProjectCodeStream() {
        Flux<String> codeStream = aiCodeGeneratorFacade.generateAndSaveCodeStream(
                "重新做一个简单的任务记录网站，总代码量不超过 200 行",
                CodeGenTypeEnum.VUE_PROJECT, 1L);
        // 阻塞等待所有数据收集完成
        List<String> result = codeStream.collectList().block();
        // 验证结果
        Assertions.assertNotNull(result);
        String completeContent = String.join("", result);
        Assertions.assertNotNull(completeContent);
    }

}