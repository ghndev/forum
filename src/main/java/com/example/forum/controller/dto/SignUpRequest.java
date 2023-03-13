package com.example.forum.controller.dto;

import com.example.forum.domain.Role;
import com.example.forum.domain.User;
import com.example.forum.validator.Username;
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

    @Username
    @Length(min = 3, max = 20)
    private String username;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}",
            message = "영문, 숫자를 조합하여 최소 8자리 이상이여야 합니다")
    private String password;

    public User toEntity() {
        return User.builder()
                .username(this.username)
                .email(this.email)
                .password(this.password)
                .role(Role.USER)
                .build();
    }
}
