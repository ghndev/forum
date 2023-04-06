package com.example.forum.controller;

import com.example.forum.controller.dto.SignUpRequest;
import com.example.forum.domain.Role;
import com.example.forum.domain.User;
import com.example.forum.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Test
    void testSignupForm() throws Exception {
        mockMvc.perform(get("/signup"))
                .andExpect(model().attributeExists("signUpRequest"))
                .andExpect(view().name("signup"))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateUserWithValidRequest() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("test@test.com");
        signUpRequest.setUsername("testUser");
        signUpRequest.setPassword("password1");

        mockMvc.perform(post("/signup")
                .with(csrf())
                .flashAttr("signUpRequest", signUpRequest))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void testCreateUserWithInvalidTest() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("test");
        signUpRequest.setUsername("s");
        signUpRequest.setPassword("short");

        mockMvc.perform(post("/signup")
                        .with(csrf())
                        .flashAttr("signUpRequest", signUpRequest))
                .andExpect(view().name("signup"))
                .andExpect(model().attributeHasFieldErrors("signUpRequest", "email", "username", "password"))
                .andExpect(status().isOk());
    }

    @Test
    void login() throws Exception {
        String username = "loginTest";
        String password = "password1";

        User user = User.builder()
                .username(username)
                .password(password)
                .email("test@test.com")
                .role(Role.USER)
                .build();

        userService.createUser(user);

        mockMvc.perform(formLogin().user(username).password(password))
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
}