package com.likelion.hufsting.domain.alarm.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum AlarmType {
    ACCEPT("매칭 수락"),
    NEW("매칭 요청");

    private final String value;
    AlarmType(String value){
        this.value = value;
    }

    @JsonCreator
    public static AlarmType deserializerAlarmType(String value){
        for(AlarmType alarmType : AlarmType.values()){
            if(alarmType.getValue().equals(value)){
                return alarmType;
            }
        }
        return null;
    }

    @JsonValue
    public String serializerAlarmType(){
        return this.value;
    }
}
