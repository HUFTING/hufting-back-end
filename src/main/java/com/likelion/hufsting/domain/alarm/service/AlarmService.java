package com.likelion.hufsting.domain.alarm.service;

import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.domain.Member.repository.MemberRepository;
import com.likelion.hufsting.domain.alarm.domain.Alarm;
import com.likelion.hufsting.domain.alarm.dto.FindAlarmsData;
import com.likelion.hufsting.domain.alarm.dto.FindAlarmsResponse;
import com.likelion.hufsting.domain.alarm.repository.AlarmRepository;
import com.likelion.hufsting.domain.alarm.repository.query.AlarmQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AlarmService {
    // constant
    // repositories
    private final MemberRepository memberRepository;
    private final AlarmQueryRepository alarmQueryRepository;

    public FindAlarmsResponse findMyAlarms(Authentication authentication){
        // get login user
        Member owner = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Not Found: " + authentication.getName()));
        // get alarms
        List<Alarm> findAlarms = alarmQueryRepository.findByOwner(owner);
        // create FindAlarmsResponse
        List<FindAlarmsData> convertedData = findAlarms.stream()
                .map((alarm) -> FindAlarmsData.builder()
                        .title(alarm.getMatchingPost().getTitle())
                        .alarmType(alarm.getAlarmType())
                        .createdAt(alarm.getCreatedAt())
                        .build()).toList();
        return new FindAlarmsResponse(convertedData.size(), convertedData);
    }
}
