package com.likelion.hufsting.domain.profile.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum Mbti {
    ESFP("ESFP"),
    ENFP("ENFP"),
    ISFP("ISFP"),
    INFP("INFP"),
    ESFJ("ESFJ"),
    ENFJ("ENFJ"),
    ISFJ("ISFJ"),
    INFJ("INFJ"),
    ESTP("ESTP"),
    ENTP("ENTP"),
    ISTP("ISTP"),
    INTP("INTP"),
    ESTJ("ESTJ"),
    ENTJ("ENTJ"),
    ISTJ("ISTJ"),
    INTJ("INTJ");

    private final String value;

    Mbti(String value){
        this.value = value;
    }

    @JsonCreator
    public static Mbti deserializerMbti(String value){
        for(Mbti mbti : Mbti.values()){
            if(mbti.getValue().equals(value)){
                return mbti;
            }
        }
        return null;
    }

    @JsonValue
    public String serializerMbti(){
        return this.value;
    }
}
