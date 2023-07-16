package com.example.canary.core.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;

/**
 * my commandLineRunner
 *
 * @author zhaohongliang 2023-07-12 16:34
 * @since 1.0
 */
@Slf4j
// @Component
public class MyCommandLineRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        log.info("MyCommandLineRunner");
    }
}
