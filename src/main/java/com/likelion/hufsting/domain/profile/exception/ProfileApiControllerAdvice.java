package com.likelion.hufsting.domain.profile.exception;

import com.likelion.hufsting.global.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ProfileApiControllerAdvice {
    private final String MULTI_FIELD_ERROR = "multiField";
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<com.likelion.hufsting.global.dto.ErrorResponse> handleMethodArgumentationValidExceptions(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors()
                .forEach((error) -> {
                    if(error instanceof FieldError){
                        errors.put(((FieldError)error).getField(), error.getDefaultMessage());
                    }else{
                        errors.put(MULTI_FIELD_ERROR, error.getDefaultMessage());
                    }
                });
        ErrorResponse body = ErrorResponse.builder()
                .errorMessages(errors)
                .build();
        return ResponseEntity.badRequest().body(body);
    }
}
