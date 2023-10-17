package com.example.canary;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

/**
 * @author zhaohongliang
 */
@SpringBootApplication
public class CanaryApplication {

	public static void main(String[] args) {
		SpringApplication.run(CanaryApplication.class, args);
	}

	/**
	 * 设置时区
	 */
	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
	}

}
