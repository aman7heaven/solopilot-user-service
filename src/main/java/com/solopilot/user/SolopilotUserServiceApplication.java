package com.solopilot.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.autopilot", "com.solopilot.user"})
public class SolopilotUserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SolopilotUserServiceApplication.class, args);
	}

}
