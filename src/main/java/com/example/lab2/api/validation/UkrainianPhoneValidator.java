package com.example.lab2.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Валідатор для кастомної анотації @UkrainianPhone.
 */
public class UkrainianPhoneValidator implements ConstraintValidator<UkrainianPhone, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.matches("^\\+380\\d{9}$");
    }
}
