package com.likelion.hufsting.domain.alarm.service;

import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.domain.Member.repository.MemberRepository;
import com.likelion.hufsting.domain.alarm.domain.Alarm;
import com.likelion.hufsting.domain.alarm.dto.FindAlarmResponse;
import com.likelion.hufsting.domain.alarm.dto.FindAlarmsData;
import com.likelion.hufsting.domain.alarm.dto.FindAlarmsResponse;
import com.likelion.hufsting.domain.alarm.repository.AlarmRepository;
import com.likelion.hufsting.domain.alarm.repository.query.AlarmQueryRepository;
import com.likelion.hufsting.domain.alarm.validation.AlarmValidator;
import com.likelion.hufsting.domain.matching.domain.MatchingPost;
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
    private final AlarmRepository alarmRepository;
    private final AlarmQueryRepository alarmQueryRepository;
    // validators
    private final AlarmValidator alarmValidator;

    public FindAlarmsResponse findMyAlarms(Authentication authentication){
        // get login user
        Member owner = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Not Found: " + authentication.getName()));
        // get alarms
        List<Alarm> findAlarms = alarmQueryRepository.findByOwner(owner);
        // create FindAlarmsResponse
        List<FindAlarmsData> convertedData = findAlarms.stream()
                .map((alarm) -> FindAlarmsData.builder()
                        .id(alarm.getMatchingPost().getId())
                        .title(alarm.getMatchingPost().getTitle())
                        .alarmType(alarm.getAlarmType())
                        .createdAt(alarm.getCreatedAt())
                        .build()).toList();
        return new FindAlarmsResponse(convertedData.size(), convertedData);
    }

    public FindAlarmResponse findMyAlarm(Long alarmId, Authentication authentication){
        // get login user
        Member owner = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Not Found: " + authentication.getName()));
        // get alarm
        Alarm findAlarm = alarmRepository.findById(alarmId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found: " + alarmId));
        // validation-0 : 매칭 완료 타입의 알람 유효성 검사
        alarmValidator.validateMatchingCompletedAlarmType(findAlarm);
        // validation-1 : 매칭 호스트 또는 매칭 참가자 확인
        alarmValidator.validateIsAlarmOwner(owner, findAlarm);
        // get matchingPost
        MatchingPost matchingPost = findAlarm.getMatchingPost();
        // return value
        return FindAlarmResponse.builder()
                .id(alarmId)
                .openTalkLink(matchingPost.getOpenTalkLink())
                .build();
    }
}
