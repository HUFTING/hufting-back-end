package com.likelion.hufsting.global.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NullPossibleEnumFormatValidator implements ConstraintValidator<NullPossibleEnumFormat, Enum> {
    private Class<? extends Enum> enumClass;

    @Override
    public void initialize(NullPossibleEnumFormat constraintAnnotation) {
        enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(Enum value, ConstraintValidatorContext context) {
        return (value == null) || isContainsValue(value);
    }

    private boolean isContainsValue(Enum value){
        List<Enum> enumValues = Arrays.stream(enumClass.getEnumConstants()).collect(Collectors.toList());
        return enumValues.contains(value);
    }
}
