package com.chipswu.aiapplicationgenerator.core;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.chipswu.aiapplicationgenerator.ai.AiCodeGeneratorService;
import com.chipswu.aiapplicationgenerator.ai.AiCodeGeneratorServiceFactory;
import com.chipswu.aiapplicationgenerator.ai.message.AiResponseMessage;
import com.chipswu.aiapplicationgenerator.ai.message.ToolExecutedMessage;
import com.chipswu.aiapplicationgenerator.ai.message.ToolRequestMessage;
import com.chipswu.aiapplicationgenerator.ai.model.HtmlCodeResult;
import com.chipswu.aiapplicationgenerator.ai.model.MultiFileCodeResult;
import com.chipswu.aiapplicationgenerator.core.parser.CodeParserExecutor;
import com.chipswu.aiapplicationgenerator.core.saver.CodeFileSaverExecutor;
import com.chipswu.aiapplicationgenerator.exception.BusinessException;
import com.chipswu.aiapplicationgenerator.exception.ErrorCode;
import com.chipswu.aiapplicationgenerator.modal.enums.CodeGenTypeEnum;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.tool.ToolExecution;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.File;

/**
 * AI 代码生成门面类，组合代码生成和保存功能
 *
 * @author WuJiaJun
 */
@Slf4j
@Service
public class AiCodeGeneratorFacade {

    @Resource
    private AiCodeGeneratorServiceFactory aiCodeGeneratorServiceFactory;

    /**
     * 统一入口，根据类型生成并保存代码
     *
     * @param userMessage     用户提示词
     * @param codeGenTypeEnum 代码生成类型
     * @param appId           应用 ID
     * @return 保存的文件目录
     */
    public File generateAndSaveCode(String userMessage,
                                    CodeGenTypeEnum codeGenTypeEnum,
                                    Long appId
    ) {
        if (StrUtil.isBlank(userMessage)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户提示词不能为空");
        }
        if (codeGenTypeEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "生成类型不能为空");
        }
        // 根据 appId 获取相应的 AI 服务实例
        AiCodeGeneratorService aiCodeGeneratorService = aiCodeGeneratorServiceFactory.getAiCodeGeneratorService(appId, codeGenTypeEnum);
        return switch (codeGenTypeEnum) {
            case HTML -> {
                HtmlCodeResult htmlCodeResult = aiCodeGeneratorService.generateHtmlCode(userMessage);
                yield CodeFileSaverExecutor.executeSaver(htmlCodeResult, CodeGenTypeEnum.HTML, appId);
            }
            case MULTI_FILE -> {
                MultiFileCodeResult multiFileCodeResult = aiCodeGeneratorService.generateMultiFileCode(userMessage);
                yield CodeFileSaverExecutor.executeSaver(multiFileCodeResult, CodeGenTypeEnum.MULTI_FILE, appId);
            }
            case VUE_PROJECT -> {
                yield null;
                /*Flux<String> codeStream = aiCodeGeneratorService.generateVueProjectCode(appId, userMessage);
                // todo：临时使用 multi_file 进行占位
                yield processCodeStream(codeStream, CodeGenTypeEnum.MULTI_FILE, appId);*/
            }
        };
    }

    /**
     * 统一入口，根据类型生成并保存代码（流式）
     *
     * @param userMessage     用户提示词
     * @param codeGenTypeEnum 代码生成类型
     * @param appId           应用 ID
     * @return 保存的文件目录
     */
    public Flux<String> generateAndSaveCodeStream(String userMessage,
                                                  CodeGenTypeEnum codeGenTypeEnum,
                                                  Long appId
    ) {
        if (StrUtil.isBlank(userMessage)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户提示词不能为空");
        }
        if (codeGenTypeEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "生成类型不能为空");
        }
        // 根据 appId 获取相应的 AI 服务实例
        AiCodeGeneratorService aiCodeGeneratorService = aiCodeGeneratorServiceFactory.getAiCodeGeneratorService(appId, codeGenTypeEnum);
        return switch (codeGenTypeEnum) {
            case HTML -> {
                Flux<String> codeStream = aiCodeGeneratorService.generateHtmlCodeStream(userMessage);
                yield processCodeStream(codeStream, CodeGenTypeEnum.HTML, appId);
            }
            case MULTI_FILE -> {
                Flux<String> codeStream = aiCodeGeneratorService.generateMultiFileCodeStream(userMessage);
                yield processCodeStream(codeStream, CodeGenTypeEnum.MULTI_FILE, appId);
            }
            case VUE_PROJECT -> {
                TokenStream tokenStream = aiCodeGeneratorService.generateVueProjectCodeStream(appId, userMessage);
                yield processTokenStream(tokenStream);
            }
        };
    }

    /**
     * 将 TokenStream 转换为 Flux<String>，并传递工具调用信息
     *
     * @param tokenStream TokenStream 对象
     * @return Flux<String> 流式响应
     */
    private Flux<String> processTokenStream(TokenStream tokenStream) {
        return Flux.create(sink -> {
            tokenStream.onPartialResponse((String partialResponse) -> {
                        AiResponseMessage aiResponseMessage = new AiResponseMessage(partialResponse);
                        sink.next(JSONUtil.toJsonStr(aiResponseMessage));
                    })
                    .onPartialToolCall((partialToolCallHandler) -> {
                        ToolRequestMessage toolRequestMessage = new ToolRequestMessage(partialToolCallHandler);
                        sink.next(JSONUtil.toJsonStr(toolRequestMessage));
                    })
                    .onToolExecuted((ToolExecution toolExecution) -> {
                        ToolExecutedMessage toolExecutedMessage = new ToolExecutedMessage(toolExecution);
                        sink.next(JSONUtil.toJsonStr(toolExecutedMessage));
                    })
                    .onCompleteResponse((ChatResponse response) -> {
                        sink.complete();
                    })
                    .onError((Throwable error) -> {
                        error.printStackTrace();
                        sink.error(error);
                    })
                    .start();
        });
    }


    /**
     * 通用流式代码处理方法
     *
     * @param codeStream  代码流
     * @param codeGenType 代码生成类型
     * @param appId       应用 ID
     * @return 流式响应
     */
    private Flux<String> processCodeStream(Flux<String> codeStream, CodeGenTypeEnum codeGenType, Long appId) {
        StringBuilder codeBuilder = new StringBuilder();
        return codeStream
                // 实时收集代码片段
                .doOnNext(codeBuilder::append)
                .doOnComplete(() -> {
                    // 流式返回完成后保存代码
                    try {
                        String completeCode = codeBuilder.toString();
                        // 使用执行器解析代码
                        Object parsedResult = CodeParserExecutor.executeParser(completeCode, codeGenType);
                        // 使用执行器保存代码
                        File savedDir = CodeFileSaverExecutor.executeSaver(parsedResult, codeGenType, appId);
                        log.info("{}生成文件保存成功，路径为：{}",
                                codeGenType.getText(),
                                savedDir.getAbsolutePath());
                    } catch (Exception e) {
                        log.error("{}生成文件保存失败，异常信息: {}",
                                codeGenType.getText(),
                                e.getMessage());
                    }
                });
    }


    /**
     * 生成单文件 HTML 模型的代码并保存（流式）
     *
     * @param userMessage 用户提示词
     * @return 保存的目录
     */
    /*@Deprecated
    private Flux<String> generateAndSaveHtmlCodeStream(String userMessage) {
        Flux<String> result = aiCodeGeneratorService.generateHtmlCodeStream(userMessage);
        // 字符串拼接器，用于当流式返回所有的代码后，再保存代码
        StringBuilder codeBuilder = new StringBuilder();
        return result
                // 实时收集代码片段
                .doOnNext(codeBuilder::append)
                // 流式所有数据返回后，保存代码
                .doOnComplete(() -> {
                    try {
                        String completeHtmlCode = codeBuilder.toString();
                        // 解析代码为对象
                        HtmlCodeResult htmlCodeResult = CodeParser.parseHtmlCode(completeHtmlCode);
                        // 保存代码到文件
                        File saveDir = CodeFileSaver.saveHtmlCodeResult(htmlCodeResult);
                        log.info("单文件保存成功，目录为：{}", saveDir.getAbsolutePath());
                    } catch (Exception e) {
                        log.error("单文件保存失败，异常消息：{}", e.getMessage());
                    }
                });
    }*/

    /**
     * 生成多文件模型的代码并保存（流式）
     *
     * @param userMessage 用户提示词
     * @return 保存的目录
     */
    /*@Deprecated
    private Flux<String> generateAndSaveMultiFileCodeStream(String userMessage) {
        Flux<String> result = aiCodeGeneratorService.generateMultiFileCodeStream(userMessage);
        // 字符串拼接器，用于当流式返回所有的代码后，再保存代码
        StringBuilder codeBuilder = new StringBuilder();
        return result
                // 实时收集代码片段
                .doOnNext(codeBuilder::append)
                // 流式所有数据返回后，保存代码
                .doOnComplete(() -> {
                    try {
                        String completeMultiFileCode = codeBuilder.toString();
                        // 解析代码为对象
                        MultiFileCodeResult multiFileCodeResult = CodeParser.parseMultiFileCode(completeMultiFileCode);
                        // 保存代码到文件
                        File saveDir = CodeFileSaver.saveMultiFileCodeResult(multiFileCodeResult);
                        log.info("多文件保存成功，目录为：{}", saveDir.getAbsolutePath());
                    } catch (Exception e) {
                        log.error("多文件保存失败，异常消息：{}", e.getMessage());
                    }
                });
    }*/

    /**
     * 生成单文件 HTML 模型的代码并保存
     *
     * @param userMessage 用户提示词
     * @return 保存的目录
     */
    /*@Deprecated
    private File generateAndSaveHtmlCode(String userMessage) {
        HtmlCodeResult htmlCodeResult = aiCodeGeneratorService.generateHtmlCode(userMessage);
        return CodeFileSaver.saveHtmlCodeResult(htmlCodeResult);
    }*/

    /**
     * 生成多文件模型的代码并保存
     *
     * @param userMessage 用户提示词
     * @return 保存的目录
     */
    /*@Deprecated
    private File generateAndSaveMultiFileCode(String userMessage) {
        MultiFileCodeResult multiFileCodeResult = aiCodeGeneratorService.generateMultiFileCode(userMessage);
        return CodeFileSaver.saveMultiFileCodeResult(multiFileCodeResult);
    }*/
}
