package com.example.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.vaadin.artur.helpers.LaunchUtil;

/**
 * The entry point of the Spring Boot application.
 */
@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
@ComponentScan("com.example")
public class TestApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		// LaunchUtil.launchBrowserInDevelopmentMode(SpringApplication.run(TestApplication.class, args));
		SpringApplication.run(TestApplication.class, args);
	}

}
