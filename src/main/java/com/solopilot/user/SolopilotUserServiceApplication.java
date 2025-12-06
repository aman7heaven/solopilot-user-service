package com.solopilot.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.autopilot", "com.solopilot.user", "com.portfolio"})
@EntityScan(basePackages = {"com.autopilot", "com.solopilot.user", "com.portfolio"})
@EnableJpaRepositories(basePackages = {"com.autopilot", "com.solopilot.user", "com.portfolio"})
public class SolopilotUserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SolopilotUserServiceApplication.class, args);
	}

}
