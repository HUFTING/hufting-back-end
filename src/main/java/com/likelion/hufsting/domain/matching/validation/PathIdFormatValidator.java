package com.likelion.hufsting.domain.matching.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PathIdFormatValidator implements ConstraintValidator<PathIdFormat, Long> {
    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return value >= 0;
    }
}
