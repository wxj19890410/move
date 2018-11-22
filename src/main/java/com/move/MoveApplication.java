package com.move;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import com.move.utils.LoginUserInfoMethodArgumentResolver;

@SpringBootApplication
public class MoveApplication extends SpringBootServletInitializer {

	/*
	 * @Override protected SpringApplicationBuilder
	 * configure(SpringApplicationBuilder builder) { return
	 * builder.sources(MoveApplication.class); }
	 */

	public static void main(String[] args) {
		SpringApplication.run(MoveApplication.class, args);
	}


}
