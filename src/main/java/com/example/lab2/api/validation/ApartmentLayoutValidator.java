package com.example.lab2.api.validation;

import com.example.lab2.api.dto.ApartmentRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

/**
 * Валідатор для кастомної анотації @ApartmentLayout.
 */
public class ApartmentLayoutValidator implements ConstraintValidator<ApartmentLayout, ApartmentRequest> {

    @Override
    public boolean isValid(ApartmentRequest value, ConstraintValidatorContext context) {
        if (value == null || value.getRooms() == null || value.getArea() == null) {
            return true;
        }

        BigDecimal minArea = getMinArea(value.getRooms());

        if (value.getArea().compareTo(minArea) >= 0) {
            return true;
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
                        "Для " + value.getRooms() + " кімн. площа має бути не меншою за " + minArea + " м²"
                )
                .addPropertyNode("area")
                .addConstraintViolation();

        return false;
    }

    private BigDecimal getMinArea(int rooms) {
        if (rooms == 1) {
            return new BigDecimal("18.0");
        }

        if (rooms == 2) {
            return new BigDecimal("35.0");
        }

        return new BigDecimal("50.0");
    }
}
