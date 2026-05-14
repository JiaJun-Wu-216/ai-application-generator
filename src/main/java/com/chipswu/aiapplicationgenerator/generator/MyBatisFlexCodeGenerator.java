package com.chipswu.aiapplicationgenerator.generator;

import cn.hutool.core.lang.Dict;
import cn.hutool.setting.yaml.YamlUtil;
import com.mybatisflex.codegen.Generator;
import com.mybatisflex.codegen.config.GlobalConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.util.Map;

public class MyBatisFlexCodeGenerator {

    /**
     * 需要生成代码的表名
     */
    private static final String[] TABLE_NAMES = {"user"};

    public static void main(String[] args) {
        // 获取元信息
        Dict dict = YamlUtil.loadByPath("application.yaml");
        Map<String, Object> dataSourceConfig = dict.getByPath("spring.datasource");
        String url = String.valueOf(dataSourceConfig.get("url"));
        String username = String.valueOf(dataSourceConfig.get("username"));
        String password = String.valueOf(dataSourceConfig.get("password"));

        // 配置数据源
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        // 创建配置内容
        GlobalConfig globalConfig = createGlobalConfig();

        // 通过 datasource 和 globalConfig 创建代码生成器
        Generator generator = new Generator(dataSource, globalConfig);

        // 生成代码
        generator.generate();
    }

    public static GlobalConfig createGlobalConfig() {
        // 创建配置内容
        GlobalConfig globalConfig = new GlobalConfig();

        // 设置根包（先生成到临时目录下）
        globalConfig.getPackageConfig()
                .setBasePackage("com.chipswu.aiapplicationgenerator.generator.generresult");

        // 设置表前缀和只生成哪些表，setGenerateTable 未配置时，生成所有表
        globalConfig.getStrategyConfig()
                .setGenerateTable(TABLE_NAMES)
                // 设置逻辑删除的默认字段名称
                .setLogicDeleteColumn("isDelete");

        // 设置生成 entity 并启用 Lombok
        globalConfig.enableEntity()
                .setWithLombok(true)
                .setJdkVersion(21);

        // 设置生成 Service
        globalConfig.enableService();

        // 设置生成 ServiceImpl
        globalConfig.enableServiceImpl();

        // 设置生成 mapper
        globalConfig.enableMapper();

        // 设置生成 mapper.xml 文件
        globalConfig.enableMapperXml();

        // 设置生成 Controller
        globalConfig.enableController();

        // 设置生成注释
        globalConfig.getJavadocConfig()
                // 作者名称
                .setAuthor("WuJiaJun")
                // 生成时间（不生成）
                .setSince("");

        return globalConfig;
    }
}