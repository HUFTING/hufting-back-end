package com.likelion.hufsting.domain.matching.validation;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumFormatValidator.class)
public @interface EnumFormat {
    String message() default "해당 필드 타입에서 지원하지 않는 값입니다.";
    Class[] groups() default {};
    Class[] payload() default {};

    // main point : get enum class
    Class<? extends Enum> enumClass();
}
