package com.likelion.hufsting.domain.matching.validation;

import com.likelion.hufsting.domain.matching.domain.MatchingStatus;
import com.likelion.hufsting.domain.matching.exception.MatchingPostException;
import org.springframework.stereotype.Component;

@Component
public class MatchingPostMethodValidator {
    private final String STATUS_ALREADY_COMPLETED = "이미 매칭 완료된 글입니다.";
    // 매칭글 상태 확인
    public void validateMatchingPostStatus(MatchingStatus matchingStatus){
        if(matchingStatus.equals(MatchingStatus.COMPLETED)){
            throw new MatchingPostException(STATUS_ALREADY_COMPLETED);
        }
    }
}
