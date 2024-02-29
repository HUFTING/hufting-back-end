package com.likelion.hufsting.domain.Member.exception;

import jakarta.servlet.ServletException;

public class JwtAuthenticationException extends ServletException {
    public JwtAuthenticationException(String message) {
        super(message);
    }
}
