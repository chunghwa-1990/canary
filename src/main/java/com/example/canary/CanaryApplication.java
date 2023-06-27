package com.example.canary;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.example.canary.*.dao")
@SpringBootApplication
public class CanaryApplication {

	public static void main(String[] args) {
		SpringApplication.run(CanaryApplication.class, args);
	}

}
