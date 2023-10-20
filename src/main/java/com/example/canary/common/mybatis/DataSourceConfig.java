package com.example.canary.common.mybatis;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据源
 *
 * @author zhaohongliang 2023-10-16 15:33
 * @since 1.0
 */
@Configuration
@ConditionalOnProperty(value = "spring.datasource.cluster.enabled")
@EnableConfigurationProperties
public class DataSourceConfig {

    /**
     * master datasource
     *
     * @return
     */
    @Bean(name = "masterDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.cluster.master")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    /**
     * slave1 datasource
     *
     * @return
     */
    @Bean(name = "slave1DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.cluster.slave1")
    public DataSource slave1DataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    /**
     * slave2 datasource
     *
     * @return
     */
    @Bean(name = "slave2DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.cluster.slave2")
    public DataSource slave2DataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    /**
     * 动态数据源路由
     *
     * @param masterDataSource
     * @param slave1DataSource
     * @param slave2DataSource
     * @return
     */
    @Primary
    @Bean(name = "dynamicRoutingDataSource")
    public AbstractRoutingDataSource dynamicRoutingDataSource(@Qualifier("masterDataSource") DataSource masterDataSource,
                                                       @Qualifier("slave1DataSource") DataSource slave1DataSource,
                                                       @Qualifier("slave2DataSource") DataSource slave2DataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DataSourceEnum.MASTER, masterDataSource);
        targetDataSources.put(DataSourceEnum.SLAVE1, slave1DataSource);
        targetDataSources.put(DataSourceEnum.SLAVE2, slave2DataSource);
        DynamicRoutingDataSource routingDataSource = new DynamicRoutingDataSource();
        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(masterDataSource);
        return routingDataSource;
    }

    /**
     * sqlSessionFactory
     *
     * @param dynamicDataSource
     * @param mybatisPlusInterceptor
     * @param readWriteInterceptor
     * @param mybatisPlusProperties
     * @return
     * @throws Exception
     */
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dynamicRoutingDataSource") DataSource dynamicDataSource,
                                               @Qualifier("mybatisPlusInterceptor") MybatisPlusInterceptor mybatisPlusInterceptor,
                                               @Qualifier("readWriteInterceptor") ReadWriteInterceptor readWriteInterceptor,
                                               MybatisPlusProperties mybatisPlusProperties) throws Exception {
        // mybatis
        // SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        // sessionFactoryBean.setDataSource(dynamicDataSource);
        // sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));

        // mybatis-plus
        MybatisSqlSessionFactoryBean sessionFactoryBean = new MybatisSqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dynamicDataSource);
        sessionFactoryBean.setMapperLocations(mybatisPlusProperties.resolveMapperLocations());
        sessionFactoryBean.setConfiguration(mybatisPlusProperties.getConfiguration());
        sessionFactoryBean.setGlobalConfig(mybatisPlusProperties.getGlobalConfig());
        sessionFactoryBean.setTypeAliasesPackage(mybatisPlusProperties.getTypeAliasesPackage());
        sessionFactoryBean.setPlugins(mybatisPlusInterceptor, readWriteInterceptor);
        return sessionFactoryBean.getObject();
    }

    /**
     * 事物管理器
     *
     * @param dynamicDataSource
     * @return
     */
    @Bean("transactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("dynamicRoutingDataSource") DataSource dynamicDataSource) {
        return new DataSourceTransactionManager(dynamicDataSource);
    }

    /**
     * master jdbc template
     *
     * @param dataSource
     * @return
     */
    @Bean(name = "masterJdbcTemplate")
    public JdbcTemplate masterJdbcTemplate(@Qualifier("masterDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    /**
     * slave1 jdbc template
     *
     * @param dataSource
     * @return
     */
    @Bean(name = "slave1JdbcTemplate")
    public JdbcTemplate slaveJdbcTemplate1(@Qualifier("slave1DataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    /**
     * slave2 jdbc template
     *
     * @param dataSource
     * @return
     */
    @Bean(name = "slave2JdbcTemplate")
    public JdbcTemplate slaveJdbcTemplate2(@Qualifier("slave2DataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
