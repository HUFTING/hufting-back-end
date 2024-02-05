package com.likelion.hufsting.domain.Member.exception;

public class MemberRequestException extends IllegalArgumentException{

    public MemberRequestException(String message){
        super(message);
    }
}
