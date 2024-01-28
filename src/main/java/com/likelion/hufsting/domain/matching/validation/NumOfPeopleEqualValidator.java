package com.likelion.hufsting.domain.matching.validation;

import com.likelion.hufsting.domain.matching.domain.MatchingHost;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ServerErrorException;

import java.lang.reflect.Field;
import java.util.List;

@Slf4j
public class NumOfPeopleEqualValidator implements ConstraintValidator<NumOfPeopleEqual, Object> {
    private String numField;
    private String listField;

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        int desiredNumPeople = getNumFieldValue(value, numField);
        int participantsSize = getListFieldSizeValue(value, listField);
        return desiredNumPeople == participantsSize;
    }

    @Override
    public void initialize(NumOfPeopleEqual constraintAnnotation) {
        numField = constraintAnnotation.numField();
        listField = constraintAnnotation.listField();
    }

    // get numField value of field by using reflection
    private int getNumFieldValue(Object object, String fieldName){
        Class<?> clazz = object.getClass();
        try{
            Field numField = clazz.getDeclaredField(fieldName);
            numField.setAccessible(true);
            Object target = numField.get(object);
            if(!(target instanceof Integer)){
                throw new ClassCastException("casting exception");
            }
            return (int)target;
        }catch (NoSuchFieldException e){
            log.error("NoSuchFieldException", e);
            throw new ServerErrorException("Not Found Field", e);
        }catch (IllegalAccessException e){
            log.error("IllegalAccessException", e);
            throw new ServerErrorException("Can't access the field", e);
        }
    }

    // get ListField of Size by using reflection
    private int getListFieldSizeValue(Object object, String fieldName){
        Class<?> clazz = object.getClass();
        try{
            Field listField = clazz.getDeclaredField(fieldName);
            listField.setAccessible(true);
            Object target = listField.get(object);
            if(!(target instanceof List<?>)) {
                throw new ClassCastException("casting exception");
            }
            return ((List<?>) target).size();
        }catch (NoSuchFieldException e){
            log.error("NoSuchFieldException", e);
            throw new ServerErrorException("Not Found Field", e);
        }catch (IllegalAccessException e){
            log.error("IllegalAccessException", e);
            throw new ServerErrorException("Can't access the field", e);
        }
    }
}
