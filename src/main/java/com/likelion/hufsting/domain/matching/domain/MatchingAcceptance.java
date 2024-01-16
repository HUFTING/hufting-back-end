package com.likelion.hufsting.domain.matching.domain;

import lombok.Getter;

@Getter
public enum MatchingAcceptance {
    ACCEPTED("매칭 수락"),
    WAITING("매칭 대기"),
    REJECTED("매칭 거부");

    MatchingAcceptance(String value){
        this.value = value;
    }
    private final String value;
}
