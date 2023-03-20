package com.example.forum.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UsernameValidator.class)
public @interface UsernameExist {
    String message() default "이미 존재하는 아이디입니다";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
