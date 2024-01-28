package com.likelion.hufsting.domain.matching.validation;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PathIdFormatValidator.class)
public @interface PathIdFormat {
    String message() default "Path Variable(ID) 값은 0보다 커야 합니다.";
    Class[] groups() default {};
    Class[] payload() default {};
}
