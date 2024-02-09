package com.likelion.hufsting.domain.profile.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class BirthdayRangeValidator implements ConstraintValidator<BirthdayRange, Integer> {
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        int minYear = 1996;
        int maxYear = LocalDate.now().getYear() - 19;
        return value >= minYear && value <= maxYear;
    }
}
