package com.randyahx.authorizationserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class AuthorizationServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthorizationServerApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(11);
	}

	// Populate database
//	@Bean
//	CommandLineRunner run(UserDetailService userDetailService) {
//		return args -> {
////			userDetailService.saveUser(new User(null, "firstname", "lastname", "email@google.com", "password", "USER", true));
//		};
//	}
}
