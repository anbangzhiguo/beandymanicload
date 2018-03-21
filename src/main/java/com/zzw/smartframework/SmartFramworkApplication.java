package com.zzw.smartframework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com"})
public class SmartFramworkApplication {
	public static void main(String[] args) {
		SpringApplication.run(SmartFramworkApplication.class, args);
	}
}
