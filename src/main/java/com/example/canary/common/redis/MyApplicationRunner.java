package com.example.canary.common.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

/**
 * my applicationRunner
 *
 * @author zhaohongliang 2023-07-12 16:33
 * @since 1.0
 */
@Slf4j
// @Component
public class MyApplicationRunner implements ApplicationRunner {


    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("MyApplicationRunner");
    }
}
