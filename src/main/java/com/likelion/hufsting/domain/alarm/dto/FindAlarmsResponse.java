package com.likelion.hufsting.domain.alarm.dto;

import com.likelion.hufsting.global.dto.ResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FindAlarmsResponse implements ResponseDto {
    private int count;
    private List<FindAlarmsData> data;
}
