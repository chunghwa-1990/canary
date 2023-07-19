package com.example.canary.common.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

/**
 * my InitializingBean
 *
 * @author zhaohongliang 2023-07-12 16:36
 * @since 1.0
 */
@Slf4j
// @Component
public class MyInitializingBean implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("Initializing");
    }
}
