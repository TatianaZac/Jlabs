package com.example.lab2.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Кастомна анотація рівня об'єкта.
 * Перевіряє зв'язок між кількістю кімнат і площею квартири.
 */
@Documented
@Constraint(validatedBy = ApartmentLayoutValidator.class)
@Target(TYPE)
@Retention(RUNTIME)
public @interface ApartmentLayout {

    String message() default "Площа квартири не відповідає кількості кімнат";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
