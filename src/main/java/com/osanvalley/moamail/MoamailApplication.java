package com.osanvalley.moamail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MoamailApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(MoamailApplication.class, args);
	}

	// ec2 404에러 해결 코드
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(MoamailApplication.class);
	}
}
