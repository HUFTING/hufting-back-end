package com.likelion.hufsting.domain.matching.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class MatchingPostApiControllerAdvice {
    private final String MULTI_FIELD_ERROR = "multi-field";
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors()
                .forEach((error) -> {
                    //if(error instanceof ObjectError){
                    //    System.out.println(error.getDefaultMessage());
                    //    errors.put(((ObjectError)error).getObjectName(), error.getDefaultMessage());
                    //}else
                    if(error instanceof FieldError){
                        errors.put(((FieldError)error).getField(), error.getDefaultMessage());
                    }else{
                        errors.put(MULTI_FIELD_ERROR, error.getDefaultMessage());
                    }
                    System.out.println(error);
                });
        System.out.println(errors.size());
        return ResponseEntity.badRequest().body(errors);
    }
}
