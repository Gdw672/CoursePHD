package com.example.CourcePHD;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"controller"})
public class CourcePhdApplication {
	public static void main(String[] args) {
		SpringApplication.run(CourcePhdApplication.class, args);
	}
}
