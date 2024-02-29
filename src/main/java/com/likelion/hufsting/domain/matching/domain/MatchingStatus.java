package com.likelion.hufsting.domain.matching.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum MatchingStatus {
    WAITING("매칭 대기"),
    COMPLETED("매칭 완료");

    MatchingStatus(String value){
        this.value = value;
    }
    private final String value;

    @JsonCreator
    public static MatchingStatus deserializerMatchingStatus(String value){
        for(MatchingStatus matchingStatus : MatchingStatus.values()){
            if(matchingStatus.getValue().equals(value)){
                return matchingStatus;
            }
        }
        return null;
    }

    @JsonValue
    public String serializerMatchingStatus(){
        return value;
    }
}
