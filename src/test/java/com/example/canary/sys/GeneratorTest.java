package com.example.canary.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.example.canary.common.context.CanaryContext;
import com.example.canary.sys.core.GeneratorConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * GeneratorTest
 *
 * @author zhaohongliang 2023-07-19 16:15
 * @since 1.0
 */
@Slf4j
class GeneratorTest {

    /**
     * 数据源配置
     */
    private static final DataSourceConfig DATA_SOURCE_CONFIG = new DataSourceConfig
            .Builder("jdbc:mysql://localhost:3306/canary?serverTimezone=Asia/Shanghai", "root", "Pass!234")
            .schema("canary")
            .build();

    /**
     * 执行数据库脚本
     */
    protected static void initDataSource(DataSourceConfig dataSourceConfig) throws SQLException {
        Connection conn = dataSourceConfig.getConn();
        InputStream inputStream = GeneratorConfig.class.getResourceAsStream("/sql/init.sql");
        ScriptRunner scriptRunner = new ScriptRunner(conn);
        scriptRunner.setAutoCommit(true);
        scriptRunner.runScript(new InputStreamReader(inputStream));
        conn.close();
    }

    /**
     * 策略配置
     */
    protected static StrategyConfig.Builder strategyConfig() {
        StrategyConfig.Builder builder = new StrategyConfig.Builder();
        builder.entityBuilder()
                .enableLombok()
                .idType(IdType.ASSIGN_UUID)
                .logicDeleteColumnName("is_deleted");
        return builder;
    }

    /**
     * 全局配置
     */
    protected static GlobalConfig.Builder globalConfig() {
        return new GlobalConfig.Builder()
                .author("zhaohongliang")
                .outputDir("/src/main/java");
    }

    /**
     * 包配置
     */
    protected static PackageConfig.Builder packageConfig() {
        return new PackageConfig.Builder()
                .parent("com.example.canary.ttt")
                .moduleName("xxxx")

                ;
    }

    /**
     * 模板配置
     */
    protected static TemplateConfig.Builder templateConfig() {
        return new TemplateConfig.Builder();
    }

    /**
     * 注入配置
     */
    protected static InjectionConfig.Builder injectionConfig() {
        // 测试自定义输出文件之前注入操作，该操作再执行生成代码前 debug 查看
        // return new InjectionConfig.Builder().beforeOutputFile((tableInfo, objectMap) -> log.info("tableInfo: " + tableInfo.getEntityName() + " objectMap: " + objectMap.size()));
        // 自定义生成模版参数
        Map<String, Object> paramsMap = new HashMap<>();
        // 自定义生成类
        Map<String, String> customFileMap = new HashMap<>();
        customFileMap.put("po" + File.separator + "%sPO.java", "/templates/PO.java");
        customFileMap.put("vo" + File.separator + "%sVO.java", "/templates/VO.java");
        customFileMap.put("ao" + File.separator + "%sAO.java", "/templates/AO.java");
        return new InjectionConfig.Builder().customMap(paramsMap).customFile(customFileMap);

    }


    @Test
    void test() {
        // 1、创建代码生成器
        AutoGenerator generator = new AutoGenerator(DATA_SOURCE_CONFIG);
        // 2、全局配置
        generator.global(globalConfig().build());
        // 3、包配置
        generator.packageInfo(packageConfig().build());
        // 4、策略配置
        generator.strategy(strategyConfig().build());
        // 执行
        generator.execute();
    }
}
