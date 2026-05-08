package com.example.lab2.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Кастомна анотація рівня поля.
 * Перевіряє, що телефон записаний у форматі +380XXXXXXXXX.
 */
@Documented
@Constraint(validatedBy = UkrainianPhoneValidator.class)
@Target(FIELD)
@Retention(RUNTIME)
public @interface UkrainianPhone {

    String message() default "Телефон має бути у форматі +380XXXXXXXXX";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
