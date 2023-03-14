package com.example.forum;

import com.example.forum.domain.User;
import com.example.forum.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ForumApplication {

	public static void main(String[] args) {
		SpringApplication.run(ForumApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(UserService userService) {
		return args -> userService.createUser(
				User.builder()
						.email("test@test.com")
						.username("test")
						.password("1234")
						.build()
		);
	}

}
