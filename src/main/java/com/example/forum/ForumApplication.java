package com.example.forum;

import com.example.forum.domain.Role;
import com.example.forum.domain.User;
import com.example.forum.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ForumApplication {

	public static void main(String[] args) {
		SpringApplication.run(ForumApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(UserRepository userRepository) {
		return args -> userRepository.save(
				User.builder()
						.email("test@test.com")
						.username("test")
						.password("password1")
						.role(Role.USER).build()
		);
	}

}
