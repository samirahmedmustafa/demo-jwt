package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.entity.Role;
import com.example.entity.User;
import com.example.service.AppService;

@SpringBootApplication
@ComponentScan({"com.example.service", "com.example.security", "com.example.entity"})
@EntityScan("com.example.entity")
@EnableJpaRepositories("com.example.repository")
public class DemoJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoJwtApplication.class, args);
	}
	
	@Bean
	CommandLineRunner run(AppService appService) {
		return args -> {
			appService.saveRole(new Role(null, "ROLE_USER"));
			appService.saveRole(new Role(null, "ROLE_ADMIN"));
			appService.saveRole(new Role(null, "ROLE_MANAGER"));
			appService.saveRole(new Role(null, "ROLE_SUPER_ADMIN"));

			appService.saveUser(new User(null, "sahmed", "123", null));
			appService.saveUser(new User(null, "ysamir", "123", null));
			appService.saveUser(new User(null, "msamir", "123", null));
			appService.saveUser(new User(null, "rsamir", "123", null));
			
			appService.addUserRole("sahmed", "ROLE_ADMIN");
			appService.addUserRole("rsamir", "ROLE_USER");
			appService.addUserRole("ysamir", "ROLE_MANAGER");
			appService.addUserRole("msamir", "ROLE_SUPER_ADMIN");
		};
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
