package com.financial.loan.auto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

@SpringBootApplication
public class AutoApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(AutoApplication.class);
		application.setDefaultProperties(Map.of("spring.docker.compose.enabled", "false"));
		application.run(args);
	}

}
