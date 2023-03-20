package com.example.forum.validator;

import com.example.forum.domain.User;
import com.example.forum.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class UsernameValidator implements ConstraintValidator<UsernameExist, String> {

    private final UserRepository userRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        User user = userRepository.findByUsername(value)
                .orElse(null);
        return user == null;
    }
}
