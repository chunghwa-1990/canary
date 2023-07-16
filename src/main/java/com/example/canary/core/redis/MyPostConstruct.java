package com.example.canary.core.redis;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**
 * my postConstruct
 *
 * @author zhaohongliang 2023-07-12 17:12
 * @since 1.0
 */
@Slf4j
// @Component
public class MyPostConstruct {

    @PostConstruct
    public void test() {
      log.info("MyPostConstruct");
    }
}
