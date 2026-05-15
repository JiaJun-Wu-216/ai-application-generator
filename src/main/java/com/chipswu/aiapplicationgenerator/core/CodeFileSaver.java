package com.chipswu.aiapplicationgenerator.core;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.chipswu.aiapplicationgenerator.ai.model.HtmlCodeResult;
import com.chipswu.aiapplicationgenerator.ai.model.MultiFileCodeResult;
import com.chipswu.aiapplicationgenerator.modal.enums.CodeGenTypeEnum;

import java.io.File;

/**
 * 文件保存器
 *
 * @author WuJiaJun
 */
@Deprecated
public class CodeFileSaver {
    /**
     * 文件保存的根目录
     */
    private static final String FILE_SAVE_ROOT_DIR = System.getProperty("user.dir") + "/tmp/code_output";

    /**
     * 保存 HTMl 网页代码
     *
     * @param htmlCodeResult 单文件 HTML 代码响应结果
     * @return 保存文件路径
     */
    public static File saveHtmlCodeResult(HtmlCodeResult htmlCodeResult) {
        String baseDirPath = buildUniqueDir(CodeGenTypeEnum.HTML.getValue());
        writeToFile(baseDirPath, "index.html", htmlCodeResult.getHtmlCode());
        return new File(baseDirPath);
    }

    /**
     * 保存多文件网页代码
     *
     * @param multiFileCodeResult 多文件代码响应结果
     * @return 保存文件路径
     */
    public static File saveMultiFileCodeResult(MultiFileCodeResult multiFileCodeResult) {
        String baseDirPath = buildUniqueDir(CodeGenTypeEnum.MULTI_FILE.getValue());
        writeToFile(baseDirPath, "index.html", multiFileCodeResult.getHtmlCode());
        writeToFile(baseDirPath, "style.css", multiFileCodeResult.getCssCode());
        writeToFile(baseDirPath, "script.js", multiFileCodeResult.getJsCode());
        return new File(baseDirPath);
    }

    /**
     * 构建文件的唯一路径
     * <p>
     * 格式为：tmp/code_output/{businessType}_{雪花 ID}
     *
     * @param businessType 业务类型
     * @return 文件的唯一路径
     */
    private static String buildUniqueDir(String businessType) {
        String uniqueDirName = StrUtil.format("{}_{}", businessType, IdUtil.getSnowflakeNextIdStr());
        String dirPath = FILE_SAVE_ROOT_DIR + File.separator + uniqueDirName;
        FileUtil.mkdir(dirPath);
        return dirPath;
    }

    /**
     * 写入本地文件
     *
     * @param dirPath  文件的唯一路径
     * @param fileName 文件名
     * @param content  文件内容
     */
    private static void writeToFile(String dirPath, String fileName, String content) {
        String filePath = dirPath + File.separator + fileName;
        FileUtil.writeUtf8String(content, filePath);
    }
}
