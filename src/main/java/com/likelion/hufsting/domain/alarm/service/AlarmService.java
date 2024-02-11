package com.likelion.hufsting.domain.alarm.service;

import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.domain.Member.repository.MemberRepository;
import com.likelion.hufsting.domain.alarm.domain.Alarm;
import com.likelion.hufsting.domain.alarm.domain.AlarmType;
import com.likelion.hufsting.domain.alarm.dto.FindAlarmResponse;
import com.likelion.hufsting.domain.alarm.dto.FindAlarmsData;
import com.likelion.hufsting.domain.alarm.dto.FindAlarmsResponse;
import com.likelion.hufsting.domain.alarm.exception.AlarmException;
import com.likelion.hufsting.domain.alarm.repository.AlarmRepository;
import com.likelion.hufsting.domain.alarm.repository.query.AlarmQueryRepository;
import com.likelion.hufsting.domain.alarm.validation.AlarmValidator;
import com.likelion.hufsting.domain.matching.domain.MatchingHost;
import com.likelion.hufsting.domain.matching.domain.MatchingParticipant;
import com.likelion.hufsting.domain.matching.domain.MatchingPost;
import com.likelion.hufsting.domain.matching.domain.MatchingRequest;
import com.likelion.hufsting.domain.matching.dto.matchingrequest.FindComeMatchingReqInHostData;
import com.likelion.hufsting.domain.matching.dto.matchingrequest.FindComeMatchingReqInParticipantData;
import com.likelion.hufsting.domain.matching.dto.matchingrequest.FindComeMatchingReqResponse;
import com.likelion.hufsting.global.dto.ResponseDto;
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

    public ResponseDto findMyAlarm(Long alarmId, Authentication authentication){
        // get login user
        Member loginMember = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Not Found: " + authentication.getName()));
        // get alarm
        Alarm findAlarm = alarmRepository.findById(alarmId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found: " + alarmId));
        // validation-0 : 매칭 호스트 또는 매칭 참가자 확인
        alarmValidator.validateIsAlarmOwner(loginMember, findAlarm);
        // get matchingPost
        MatchingPost matchingPost = findAlarm.getMatchingPost();
        // 매칭 완료 알람
        if(findAlarm.getAlarmType().equals(AlarmType.ACCEPT)){
            // return value
            return FindAlarmResponse.builder()
                    .id(alarmId)
                    .openTalkLink(matchingPost.getOpenTalkLink())
                    .build();
        }else if(findAlarm.getAlarmType().equals(AlarmType.NEW)){
            MatchingRequest findMatchingRequest = findAlarm.getMatchingRequest();
            // get matching participants in matching request
            List<Member> participants = findMatchingRequest.getParticipants().stream()
                    .map(MatchingParticipant::getParticipant).toList();
            List<FindComeMatchingReqInParticipantData> participantsData = participants.stream()
                    .map(FindComeMatchingReqInParticipantData::toFindComeMatchingReqInParticipantData).toList();
            // get matching hosts in matching request
            List<Member> hosts = matchingPost.getMatchingHosts().stream()
                    .map(MatchingHost::getHost).toList();
            List<FindComeMatchingReqInHostData> hostsData = hosts.stream()
                    .map(FindComeMatchingReqInHostData::toFindMatchingReqHostData).toList();
            return FindComeMatchingReqResponse.builder()
                    .matchingRequestId(findMatchingRequest.getId())
                    .matchingRequestTitle(findMatchingRequest.getTitle())
                    .participants(participantsData)
                    .hosts(hostsData)
                    .build();
        }
        throw new AlarmException("유효한 알림 타입이 아닙니다.");
    }
}
