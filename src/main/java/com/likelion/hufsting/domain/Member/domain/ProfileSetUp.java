package com.likelion.hufsting.domain.Member.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum ProfileSetUp {
    SETTING("설정 완료"),
    NOT_SETTING("설정 미완료");

    private final String value;
    ProfileSetUp(String value){
        this.value = value;
    }

    @JsonCreator
    public static ProfileSetUp deserializationProfileSetUp(String value){
        for(ProfileSetUp profileSetUp : ProfileSetUp.values()){
            if(profileSetUp.getValue().equals(value)){
                return profileSetUp;
            }
        }
        return null;
    }

    @JsonValue
    public String serializationProfileSetUp(){
        return this.value;
    }
}
