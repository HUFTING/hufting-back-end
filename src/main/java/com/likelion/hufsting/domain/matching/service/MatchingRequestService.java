package com.likelion.hufsting.domain.matching.service;

import com.likelion.hufsting.domain.Member.repository.MemberRepository;
import com.likelion.hufsting.domain.alarm.domain.Alarm;
import com.likelion.hufsting.domain.alarm.domain.AlarmType;
import com.likelion.hufsting.domain.alarm.exception.AlarmException;
import com.likelion.hufsting.domain.alarm.repository.AlarmRepository;
import com.likelion.hufsting.domain.alarm.repository.query.AlarmQueryRepository;
import com.likelion.hufsting.domain.matching.domain.*;
import com.likelion.hufsting.domain.matching.dto.matchingrequest.*;
import com.likelion.hufsting.domain.matching.repository.MatchingPostRepository;
import com.likelion.hufsting.domain.matching.repository.MatchingRequestRepository;
import com.likelion.hufsting.domain.matching.repository.query.MatchingRequestQueryRepository;

import com.likelion.hufsting.domain.matching.validation.MatchingPostMethodValidator;
import com.likelion.hufsting.domain.matching.validation.MatchingReqMethodValidator;
import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.domain.profile.validation.ProfileMethodValidator;
import com.likelion.hufsting.global.domain.Gender;
import com.likelion.hufsting.global.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchingRequestService {
    // constant
    private final String FIND_MY_ACCEPT_ALARM_ERR_MSG = "존재하지 않는 알림입니다.";
    // Repositories
    private final MatchingRequestRepository matchingRequestRepository;
    private final MatchingPostRepository matchingPostRepository;
    private final MatchingRequestQueryRepository matchingRequestQueryRepository;
    private final MemberRepository memberRepository;
    private final AlarmRepository alarmRepository;
    private final AlarmQueryRepository alarmQueryRepository;
    // Validators
    private final MatchingPostMethodValidator matchingPostMethodValidator;
    private final MatchingReqMethodValidator matchingReqMethodValidator;
    private final ProfileMethodValidator profileMethodValidator;
    // utils
    private final AuthUtil authUtil;

    // 매칭 신청 생성
    @Transactional
    public CreateMatchingReqResponse createMatchingRequests(CreateMatchingReqData dto, Authentication authentication){
        Member representative = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Not Found: " + authentication.getName()));
        // get matchingPost
        MatchingPost matchingPost = matchingPostRepository.findById(dto.getMatchingPostId())
                .orElseThrow(() -> new IllegalArgumentException("Not Found: " + dto.getMatchingPostId()));
        // validation-0 : 내 글인지 확인(* 나중에 함수로 변경)
        authUtil.isNotOwnerOfMatchingObject(representative, matchingPost.getAuthor());
        // validation-1 : DTO
        matchingReqMethodValidator.validateParticipantsField(
                dto.getParticipantIds(),
                representative.getId(),
                matchingPost.getMatchingHosts().size()
        );
        // validation-2 : MatchingPost
        matchingPostMethodValidator.validateMatchingPostStatus(
                matchingPost.getMatchingStatus()
        );
        // 멤버 ID를 통해 Member 조회
        List<Member> findParticipants = dto.getParticipantIds().stream().map(
                (participantId) -> memberRepository.findById(participantId)
                        .orElseThrow(() -> new IllegalArgumentException("Not Found: " + participantId))
        ).toList();
        // validation-3 : 성별 확인
        profileMethodValidator.validateMemberOfGender(findParticipants, Gender.toggleGender(matchingPost.getGender()));
        // validation-4 : 이미 신청한 글인지 확인
        Optional<MatchingRequest> findMatchingRequest = matchingRequestQueryRepository.findByParticipantAndPostId(representative.getId(), matchingPost.getId());
        matchingReqMethodValidator.validateAlreadyRequest(findMatchingRequest);
        // create matching request obj
        MatchingRequest newMatchingRequest = MatchingRequest.builder()
                .title(dto.getTitle())
                .matchingPost(matchingPost)
                .representative(representative)
                .matchingAcceptance(MatchingAcceptance.WAITING)
                .build();
        // matching participant 객체 생성
        List<MatchingParticipant> matchingParticipants = createMatchingParticipants(newMatchingRequest, findParticipants);
        newMatchingRequest.addParticipant(matchingParticipants);
        matchingRequestRepository.save(newMatchingRequest);
        // alarm 생성
        Alarm matchingRequestAlarm = Alarm.builder()
                        .alarmType(AlarmType.NEW)
                .matchingRequest(newMatchingRequest)
                                .matchingPost(matchingPost)
                                        .owner(matchingPost.getAuthor())
                                                .build();
        alarmRepository.save(matchingRequestAlarm);
        // return value generation
        String title = dto.getTitle();
        Long createdMatchingRequestId = newMatchingRequest.getId();
        List<Long> createdMatchingRequestParticipants = dto.getParticipantIds();
        return new CreateMatchingReqResponse(title, createdMatchingRequestId, createdMatchingRequestParticipants);
    }

    // 매칭 신청 취소
    @Transactional
    public void removeMatchingRequest(Long matchingRequestId, Authentication authentication){
        // 요청자 조회
        Member requestMember = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Not Found: " + authentication.getName()));
        MatchingRequest matchingRequest = matchingRequestRepository.findById(matchingRequestId)
                        .orElseThrow(() -> new IllegalArgumentException("Not Found: " + matchingRequestId));
        // validation-0: 글 작성자 일치 확인(* 함수로 변환)
        authUtil.isOwnerOfMatchingObject(requestMember, matchingRequest.getRepresentative());
        matchingReqMethodValidator.validateCanBeDeleted(matchingRequest.getMatchingAcceptance());
        matchingRequestRepository.delete(matchingRequest);
    }

    // 매칭 신청 수정
    @Transactional
    public UpdateMatchingReqResponse updateMatchingRequest(Long matchingRequestId, UpdateMatchingReqData dto, Authentication authentication){
        // 글 작성자 조회(* 함수로 변경)
        Member requestMember = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Not Found: " + authentication.getName()));
        // matchingRequest 조회
        MatchingRequest matchingRequest = matchingRequestRepository.findById(matchingRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found: " + matchingRequestId));
        // validation-0 : 대표자 일치 확인
        authUtil.isOwnerOfMatchingObject(requestMember, matchingRequest.getRepresentative());
        // matchingPost 조회
        MatchingPost matchingPost = matchingPostRepository.findById(dto.getMatchingPostId())
                .orElseThrow(() -> new IllegalArgumentException("Not Found: " + dto.getMatchingPostId()));
        // validation-1 : DTO
        matchingReqMethodValidator.validateParticipantsField(
                dto.getParticipantIds(),
                matchingRequest.getRepresentative().getId(),
                matchingPost.getMatchingHosts().size()
        );
        // validation-2 : MatchingRequest
        matchingReqMethodValidator.validateCanBeModified(
                matchingRequest.getMatchingAcceptance()
        );
        // 멤버 ID를 통해 Member 조회
        List<Member> findParticipants = dto.getParticipantIds().stream().map(
                (participantId) -> memberRepository.findById(participantId)
                        .orElseThrow(() -> new IllegalArgumentException("Not Found: " + participantId))
        ).toList();
        // validation-3 : 성별 확인
        profileMethodValidator.validateMemberOfGender(findParticipants, Gender.toggleGender(matchingPost.getGender()));
        // 매칭 신청 수정
        matchingRequest.updateTitle(dto.getTitle()); // 제목 수정
        matchingRequest.updateParticipant( // 참가자 수정
                createMatchingParticipants(matchingRequest, findParticipants)
        );
        return new UpdateMatchingReqResponse(dto.getTitle(), matchingRequestId, dto.getParticipantIds());
    }

    // 내 매칭 신청 현황 확인
    public FindMyMatchingReqsResponse findMyMatchingRequests(Authentication authentication){
        // 신청자 확인
        Member participant = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Not Found: " + authentication.getName()));
        List<MatchingRequest> findMyMatchingRequests = matchingRequestQueryRepository.findByParticipant(participant);
        List<FindMyMatchingReqsData> convertedMyMatchingRequests = findMyMatchingRequests.stream().map(
                FindMyMatchingReqsData::toFindMatchingReqData
        ).toList();
        return FindMyMatchingReqsResponse.builder()
                .data(convertedMyMatchingRequests)
                .build();
    }

    // 특정 내 매칭 신청 확인
    public FindComeMatchingReqResponse findComeMatchingRequest(Long matchingRequestId, Authentication authentication){
        // 로그인 유저 확인
        Member loginUser = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Not Found: " + authentication.getName()));
        // get matching request by id
        MatchingRequest findMyMatchingRequest = matchingRequestQueryRepository.findById(matchingRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found: " + matchingRequestId));
        // get matching post by req
        MatchingPost matchingPost = findMyMatchingRequest.getMatchingPost();
        // validation-0 : loginUser == matchingPost.getAuthor
        matchingReqMethodValidator.validateCanAccessToComeReq(matchingPost.getAuthor().getId(), loginUser.getId());
        // get matching participants in matching request
        List<Member> participants = findMyMatchingRequest.getParticipants().stream()
                .map(MatchingParticipant::getParticipant).toList();
        List<FindComeMatchingReqInParticipantData> participantsData = participants.stream()
                .map(FindComeMatchingReqInParticipantData::toFindComeMatchingReqInParticipantData).toList();
        // get matching hosts in matching request
        List<Member> hosts = matchingPost.getMatchingHosts().stream()
                .map(MatchingHost::getHost).toList();
        List<FindComeMatchingReqInHostData> hostsData = hosts.stream()
                .map(FindComeMatchingReqInHostData::toFindMatchingReqHostData).toList();
        // return value
        return FindComeMatchingReqResponse.builder()
                .matchingRequestId(findMyMatchingRequest.getId())
                .matchingRequestTitle(findMyMatchingRequest.getTitle())
                .matchingAcceptance(findMyMatchingRequest.getMatchingAcceptance())
                .participants(participantsData)
                .hosts(hostsData)
                .build();
    }

    // 매칭 수락
    @Transactional
    public AcceptMatchingRequestResponse acceptMatchingRequest(Long matchingRequestId, Authentication authentication){
        // 요청자 확인
        Member requestMember = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Not Found: " + authentication.getName()));
        MatchingRequest findMatchingRequest = matchingRequestRepository.findById(matchingRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found: " + matchingRequestId));
        MatchingPost findMatchingPost = findMatchingRequest.getMatchingPost();
        authUtil.isOwnerOfMatchingObject(requestMember, findMatchingPost.getAuthor());
        // validation-1 : matchingPost
        matchingPostMethodValidator.validateMatchingPostStatus(findMatchingPost.getMatchingStatus());
        // validation-2 : matchingRequest
        matchingReqMethodValidator.validateCanBeModified(
                findMatchingRequest.getMatchingAcceptance()
        );
        // 매칭글 상태 변경
        findMatchingPost.updateMatchingStatus();
        // 매칭 요청 상태 변경
        Long alarmId = null;
        List<MatchingRequest> findMatchingRequests = findMatchingPost.getMatchingRequests();
        for(MatchingRequest matchingRequest : findMatchingRequests){
            if(matchingRequest.getId().equals(matchingRequestId)){
                matchingRequest.acceptMatchingRequest();
                // accept alarm generation
                alarmId = generationAcceptAlarm(findMatchingPost, matchingRequest);
            }else{
                matchingRequest.rejectMatchingRequest();
            }
        }
        // return value
        return AcceptMatchingRequestResponse.builder()
                .matchingRequestId(matchingRequestId)
                .alarmId(alarmId)
                .matchingPostId(findMatchingPost.getId())
                .build();
    }

    // 매칭 거부
    @Transactional
    public RejectMatchingRequestResponse rejectMatchingRequest(Long matchingRequestId, Authentication authentication){
        // 요청자 확인
        Member requestMember = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Not Found: " + authentication.getName()));
        MatchingRequest findMatchingRequest = matchingRequestRepository.findById(matchingRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found: " + matchingRequestId));
        MatchingPost findMatchingPost = findMatchingRequest.getMatchingPost();
        // validation-0
        authUtil.isOwnerOfMatchingObject(requestMember, findMatchingPost.getAuthor());
        // validation-1 : matchingRequest
        matchingReqMethodValidator.validateCanBeModified(
                findMatchingRequest.getMatchingAcceptance()
        );
        // 매칭 요청 상태 변경
        findMatchingRequest.rejectMatchingRequest();
        return RejectMatchingRequestResponse.builder()
                .matchingRequestId(matchingRequestId)
                .build();
    }

    // 사용자 정의 메서드
    // create MatchingRequests List By MemberId
    private List<MatchingParticipant> createMatchingParticipants(MatchingRequest matchingRequest,List<Member> participants){
        List<MatchingParticipant> matchingParticipants = new ArrayList<>();
        for(Member participant : participants){
            matchingParticipants.add(new MatchingParticipant(matchingRequest, participant));
        }
        return matchingParticipants;
    }

    private Long generationAcceptAlarm(MatchingPost matchingPost, MatchingRequest matchingRequest){
        // 매칭 호스트 멤버
        List<Member> matchingHosts = matchingPost.getMatchingHosts().stream()
                .map(MatchingHost::getHost).toList();
        // 매칭 참가자 멤버
        List<Member> matchingParticipants = matchingRequest.getParticipants().stream()
                .map(MatchingParticipant::getParticipant).toList();
        // 알림이 필요한 전체 사용자
        List<Member> needToChangeMembers = new ArrayList<>();
        needToChangeMembers.addAll(matchingHosts);
        needToChangeMembers.addAll(matchingParticipants);
        Long alarmId = null;
        // 알림 생성
        for(Member needToChangeMember : needToChangeMembers){
            Alarm newAlarm = Alarm.builder()
                    .matchingPost(matchingPost)
                    .owner(needToChangeMember)
                    .alarmType(AlarmType.ACCEPT)
                    .build();
            alarmRepository.save(newAlarm);
            if(needToChangeMember.getId().equals(matchingPost.getAuthor().getId())){
                alarmId = newAlarm.getId();
            }
        }
        return alarmId;
    }
}
