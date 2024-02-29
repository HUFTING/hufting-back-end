package com.likelion.hufsting.domain.profile.validation;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BirthdayRangeValidator.class)
public @interface BirthdayRange {
    String message() default "유효하지 않는 입력값 입니다.";
    Class[] groups() default {};
    Class[] payload() default {};
}
