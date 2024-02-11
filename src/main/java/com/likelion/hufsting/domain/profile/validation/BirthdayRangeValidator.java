package com.likelion.hufsting.domain.profile.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class BirthdayRangeValidator implements ConstraintValidator<BirthdayRange, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        int minYear = 1996;
        int maxYear = LocalDate.now().getYear() - 19;
        if(value == null) return true;
        int birthDay = Integer.parseInt(value);
        return (birthDay >= minYear && birthDay <= maxYear);
    }
}
