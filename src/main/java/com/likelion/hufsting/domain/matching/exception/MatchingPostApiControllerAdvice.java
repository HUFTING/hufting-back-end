package com.likelion.hufsting.domain.matching.exception;

import jakarta.validation.ConstraintViolationException;
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
    private final String MULTI_FIELD_ERROR = "multiField";
    private final String PATH_OR_QUERY_ERROR_KEY = "pathOrQuery";
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentationValidExceptions(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors()
                .forEach((error) -> {
                    if(error instanceof FieldError){
                        errors.put(((FieldError)error).getField(), error.getDefaultMessage());
                    }else{
                        errors.put(MULTI_FIELD_ERROR, error.getDefaultMessage());
                    }
                });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationExceptionExceptions(ConstraintViolationException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations()
                .forEach(constraintViolation -> {
                    errors.put(PATH_OR_QUERY_ERROR_KEY, constraintViolation.getMessage());
                });
        return ResponseEntity.badRequest().body(errors);
    }
}
