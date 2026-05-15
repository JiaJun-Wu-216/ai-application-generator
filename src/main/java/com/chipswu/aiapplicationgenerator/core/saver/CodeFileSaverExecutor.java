package com.chipswu.aiapplicationgenerator.core.saver;

import com.chipswu.aiapplicationgenerator.ai.model.HtmlCodeResult;
import com.chipswu.aiapplicationgenerator.ai.model.MultiFileCodeResult;
import com.chipswu.aiapplicationgenerator.modal.enums.CodeGenTypeEnum;

import java.io.File;

/**
 * 代码文件保存执行器
 * <p>
 * 根据代码生成类型执行相应的保存逻辑
 *
 * @author WuJiaJun
 */
public class CodeFileSaverExecutor {

    private static final HtmlCodeFileSaverTemplate htmlCodeFileSaver = new HtmlCodeFileSaverTemplate();

    private static final MultiFileCodeFileSaverTemplate multiFileCodeFileSaver = new MultiFileCodeFileSaverTemplate();

    /**
     * 执行代码保存
     *
     * @param codeResult  代码结果对象
     * @param codeGenType 代码生成类型
     * @return 保存的目录
     */
    public static File executeSaver(Object codeResult, CodeGenTypeEnum codeGenType) {
        return switch (codeGenType) {
            case HTML -> htmlCodeFileSaver.saveCode((HtmlCodeResult) codeResult);
            case MULTI_FILE -> multiFileCodeFileSaver.saveCode((MultiFileCodeResult) codeResult);
        };
    }
}
