package com.likelion.hufsting.domain.matching.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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

    @JsonCreator
    public static MatchingAcceptance deserializerMatchingAcceptance(String value){
        for(MatchingAcceptance matchingAcceptance : MatchingAcceptance.values()){
            if(matchingAcceptance.getValue().equals(value)){
                return matchingAcceptance;
            }
        }
        return null;
    }

    @JsonValue
    public String serializerMatchingAcceptance(){
        return value;
    }
}