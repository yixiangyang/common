package com.xiangyang.util;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@ComponentScan(basePackages = "com.xiangyang.**")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
