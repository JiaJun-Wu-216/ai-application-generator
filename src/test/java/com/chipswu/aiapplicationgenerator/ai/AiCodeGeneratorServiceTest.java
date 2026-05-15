package com.chipswu.aiapplicationgenerator.ai;

import com.chipswu.aiapplicationgenerator.ai.model.HtmlCodeResult;
import com.chipswu.aiapplicationgenerator.ai.model.MultiFileCodeResult;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 代码生成服务测试类
 *
 * @author WuJiaJun
 */
@SpringBootTest
class AiCodeGeneratorServiceTest {

    @Resource
    private AiCodeGeneratorService aiCodeGeneratorService;

    @Test
    void generateHtmlCode() {
        HtmlCodeResult result = aiCodeGeneratorService.generateHtmlCode("做个博客，不超过 20 行");
        Assertions.assertNotNull(result);
    }

    @Test
    void generateMultiFileCode() {
        MultiFileCodeResult result = aiCodeGeneratorService.generateMultiFileCode("做个留言板，不超过 50 行");
        Assertions.assertNotNull(result);
    }
}