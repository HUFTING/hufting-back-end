package com.likelion.hufsting.domain.Member.exception;

import jakarta.servlet.ServletException;

public class ProfileSetUpStatusException extends ServletException {
    public ProfileSetUpStatusException(String message) {
        super(message);
    }
}
