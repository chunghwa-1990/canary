package com.example.canary.sys.core;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Generator
 *
 * @author zhaohongliang 2023-07-19 15:48
 * @since 1.0
 */
@Slf4j
// @Configuration
public class GeneratorConfig {

    private GeneratorConfig() {
    }

    /**
     * 数据源配置
     */
    private static final DataSourceConfig DATA_SOURCE_CONFIG = new DataSourceConfig
            .Builder("jdbc:mysql://xxxx:3306/baomidou?serverTimezone=Asia/Shanghai", "root", "123456")
            .schema("baomidou")
            .build();

    @Autowired
    private DataSourceProperties properties;

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
        return new StrategyConfig.Builder();
    }

    /**
     * 全局配置
     */
    protected static GlobalConfig.Builder globalConfig() {
        return new GlobalConfig.Builder();
    }

    /**
     * 包配置
     */
    protected static PackageConfig.Builder packageConfig() {
        return new PackageConfig.Builder();
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
        return new InjectionConfig.Builder().beforeOutputFile((tableInfo, objectMap) -> log.info("tableInfo: " + tableInfo.getEntityName() + " objectMap: " + objectMap.size()));
    }



    public void testSimple() {
        AutoGenerator generator = new AutoGenerator(DATA_SOURCE_CONFIG);
        generator.strategy(strategyConfig().build());
        generator.global(globalConfig().build());
        generator.execute();
    }
}
