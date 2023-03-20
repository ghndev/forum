package com.example.forum.controller.dto;

import com.example.forum.domain.Role;
import com.example.forum.domain.User;
import com.example.forum.validator.UsernameExist;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter @Setter
public class SignUpRequest {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    @UsernameExist
    @Length(min = 3, max = 20)
    private String username;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}", message = "영문과 숫자를 조합하여 8자 이상이여야 합니다")
    private String password;

    public User toEntity() {
        return User.builder()
                .email(this.email)
                .username(this.username)
                .password(this.password)
                .role(Role.USER)
                .build();
    }
}