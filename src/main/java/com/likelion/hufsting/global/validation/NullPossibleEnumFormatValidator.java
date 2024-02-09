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
        return isNull(value) || isContainsValue(value);
    }

    // enum 객체의 값이 NULL 인지 확인
    private boolean isNull(Enum value){
        return value == null;
    }

    private boolean isContainsValue(Enum value){
        List<Enum> enumValues = Arrays.stream(enumClass.getEnumConstants()).collect(Collectors.toList());
        return enumValues.contains(value);
    }
}
