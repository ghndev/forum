package com.example.forum.controller;

import com.example.forum.controller.dto.SignUpRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void TestSignupForm() throws Exception {
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
                .andExpect(view().name("/signup"))
                .andExpect(model().attributeHasFieldErrors("signUpRequest", "email", "username", "password"))
                .andExpect(status().isOk());
    }

}