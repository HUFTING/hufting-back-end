package com.likelion.hufsting.global.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnumFormatValidator implements ConstraintValidator<EnumFormat, Enum> {
    private Class<? extends Enum> enumClass;
    @Override
    public void initialize(EnumFormat constraintAnnotation) {
        enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(Enum value, ConstraintValidatorContext context) {
        return isNotNull(value) && isContainsValue(value);
    }

    // enum 객체의 값이 NULL 인지 확인
    private boolean isNotNull(Enum value){
        return value != null;
    }

    private boolean isContainsValue(Enum value){
        List<Enum> enumValues = Arrays.stream(enumClass.getEnumConstants()).collect(Collectors.toList());
        return enumValues.contains(value);
    }
}
