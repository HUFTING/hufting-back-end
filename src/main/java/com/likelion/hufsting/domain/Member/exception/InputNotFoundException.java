package com.likelion.hufsting.domain.Member.exception;

import org.springframework.security.core.AuthenticationException;

public class InputNotFoundException extends AuthenticationException {
    public InputNotFoundException(String msg) {
        super(msg);
    }
}
