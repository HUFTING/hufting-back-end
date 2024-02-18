package com.likelion.hufsting.domain.alarm.dto;

import com.likelion.hufsting.global.dto.ResponseDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FindAlarmResponse implements ResponseDto {
    private Long id; // alarm id
    private String openTalkLink;
}
