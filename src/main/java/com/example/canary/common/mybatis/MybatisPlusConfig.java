package com.example.canary.common.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis-plug
 *
 * @author zhaohongliang 2023-10-20 00:34
 * @since 1.0
 */
@Configuration
@MapperScan(value = "com.example.canary.*.mapper", sqlSessionFactoryRef = "sqlSessionFactory")
public class MybatisPlusConfig {

    /**
     * mybati-plus 拦截器并设置分页
     *
     * @return
     */
    @Bean("mybatisPlusInterceptor")
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        // 溢出总页数后是否进行处理，默认 false 不处理
        paginationInnerInterceptor.setOverflow(false);
        // 单页分页条数限制，默认无限制，小于0则为无限制
        paginationInnerInterceptor.setMaxLimit(-1L);
        // 开启 count 的 join 优化,只针对部分 left join
        paginationInnerInterceptor.setOptimizeJoin(true);
        // 数据库类型
        paginationInnerInterceptor.setDbType(DbType.MYSQL);

        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        return interceptor;
    }
}
