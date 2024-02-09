package com.likelion.hufsting.domain.alarm.dto;

import com.likelion.hufsting.domain.alarm.domain.AlarmType;
import com.likelion.hufsting.global.dto.ResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FindAlarmsData {
    private Long id; // 관련 매칭글 ID
    private String title; // 관련 매칭글 제목
    private AlarmType alarmType; // 알람 타입
    private LocalDateTime createdAt; // 알람 생성 시간
}
