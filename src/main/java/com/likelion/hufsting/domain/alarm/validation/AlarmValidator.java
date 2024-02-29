package com.likelion.hufsting.domain.alarm.validation;

import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.domain.alarm.domain.Alarm;
import com.likelion.hufsting.domain.alarm.domain.AlarmType;
import com.likelion.hufsting.domain.alarm.exception.AlarmException;
import org.springframework.stereotype.Component;

@Component
public class AlarmValidator {
    // constant
    private final String ALARM_TYPE_IS_NOT_ACCEPT_TYPE_ERR_MSG = "결과를 조회할 수 없는 알림입니다.";
    private final String ALARM_OWNER_NOT_MATCH_ERR_MSG = "나에게 온 알림이 아닙니다.";
    // 알람 타입 : 매칭 완료 타입 확인
    public void validateMatchingCompletedAlarmType(Alarm alarm){
        if(!alarm.getAlarmType().equals(AlarmType.ACCEPT)){
            throw new AlarmException(ALARM_TYPE_IS_NOT_ACCEPT_TYPE_ERR_MSG);
        }
    }

    // 매칭 호스트 또는 매칭 참가자 확인
    public void validateIsAlarmOwner(Member loginMember, Alarm alarm){
        if(!loginMember.getId().equals(alarm.getOwner().getId())){
            throw new AlarmException(ALARM_OWNER_NOT_MATCH_ERR_MSG);
        }
    }
}
