package com.likelion.hufsting.global.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum Gender {
    MALE("남"),
    FEMALE("여");

    Gender(String value){
        this.value = value;
    }
    private final String value;

    // 역직렬화 함수(Deserializer Function)
    @JsonCreator
    public static Gender deserializerGender(String value){
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!! 역직렬화");
        System.out.println(value);
        for(Gender gender : Gender.values()){
            if(gender.getValue().equals(value)) {
                return gender;
            }
        }
        return null;
    }

    // 직렬화 함수(Serializer Function)
    @JsonValue
    public String serializerGender(){
        return value;
    }

    public static Gender toggleGender(Gender gender){
        if(gender.getValue().equals("남")) return Gender.FEMALE;
        return Gender.MALE;
    }
}
