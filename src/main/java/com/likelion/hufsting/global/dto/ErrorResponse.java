package com.likelion.hufsting.global.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
public class ErrorResponse implements ResponseDto{
    @Builder.Default
    Map<String, String> errorMessages = new HashMap<>();

    // 에러 추가 메서드
    public void addErrorMessage(String key, String value){
        errorMessages.put(key, value);
    }

    // 단일 에러 응답 생성
    public static ErrorResponse createSingleResponseErrorMessage(String key, String value){
        ErrorResponse responseErrorMessage = ErrorResponse.builder().build();
        responseErrorMessage.addErrorMessage(key, value);
        return responseErrorMessage;
    }
}
