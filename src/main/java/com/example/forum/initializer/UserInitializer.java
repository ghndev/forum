package com.example.forum.initializer;

import com.example.forum.domain.Role;
import com.example.forum.domain.User;
import com.example.forum.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        String email = "test@test.com";
        String username = "test";

        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isEmpty()) {
            User user = User.builder()
                    .email(email)
                    .username(username)
                    .password(passwordEncoder.encode("password1"))
                    .role(Role.USER)
                    .build();

            userRepository.save(user);
        }
    }


}