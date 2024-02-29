package com.likelion.hufsting.domain.Member.validation;

import com.likelion.hufsting.domain.Member.exception.EmailDomainException;
import org.springframework.stereotype.Component;

@Component
public class GoogleAuthMethodValidator {
    private final String VALIDATION_HUFS_EMAIL_REGEX = "\\w+@hufs.ac.kr";
    private final String VALIDATION_HUFS_EMAIL_ERR_MSG = "적절한 이메일이 아닙니다.";
    public void isAppropriateEmailDomain(String email){
        if(!email.matches(VALIDATION_HUFS_EMAIL_REGEX)){
            throw new EmailDomainException(VALIDATION_HUFS_EMAIL_ERR_MSG);
        }
    }
}
