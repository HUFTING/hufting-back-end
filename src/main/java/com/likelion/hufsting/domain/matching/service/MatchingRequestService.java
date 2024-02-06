package com.likelion.hufsting.domain.matching.service;

import com.likelion.hufsting.domain.Member.repository.MemberRepository;
import com.likelion.hufsting.domain.alarm.domain.Alarm;
import com.likelion.hufsting.domain.alarm.domain.AlarmType;
import com.likelion.hufsting.domain.alarm.repository.AlarmRepository;
import com.likelion.hufsting.domain.matching.domain.*;
import com.likelion.hufsting.domain.matching.dto.matchingrequest.*;
import com.likelion.hufsting.domain.matching.repository.MatchingPostRepository;
import com.likelion.hufsting.domain.matching.repository.MatchingRequestRepository;
import com.likelion.hufsting.domain.matching.repository.query.MatchingRequestQueryRepository;

import com.likelion.hufsting.domain.matching.validation.MatchingPostMethodValidator;
import com.likelion.hufsting.domain.matching.validation.MatchingReqMethodValidator;
import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.global.exception.AuthException;
import com.likelion.hufsting.global.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchingRequestService {
    // Repositories
    private final MatchingRequestRepository matchingRequestRepository;
    private final MatchingPostRepository matchingPostRepository;
    private final MatchingRequestQueryRepository matchingRequestQueryRepository;
    private final MemberRepository memberRepository;
    private final AlarmRepository alarmRepository;
    // Validators
    private final MatchingPostMethodValidator matchingPostMethodValidator;
    private final MatchingReqMethodValidator matchingReqMethodValidator;
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
        // create matching request obj
        MatchingRequest newMatchingRequest = MatchingRequest.builder()
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
                                .matchingPost(matchingPost)
                                        .owner(matchingPost.getAuthor())
                                                .build();
        alarmRepository.save(matchingRequestAlarm);
        // return value generation
        Long createdMatchingRequestId = newMatchingRequest.getId();
        List<Long> createdMatchingRequestParticipants = dto.getParticipantIds();
        return new CreateMatchingReqResponse(createdMatchingRequestId, createdMatchingRequestParticipants);
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
        // 매칭 신청 수정
        matchingRequest.updateParticipant(
                createMatchingParticipants(matchingRequest, findParticipants)
        );

        return new UpdateMatchingReqResponse(matchingRequestId, dto.getParticipantIds());
    }

    // 내 매칭 신청 현황 확인
    public FindMyMatchingReqResponse findMyMatchingRequest(Authentication authentication){
        // 신청자 확인
        Member participant = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Not Found: " + authentication.getName()));
        List<MatchingRequest> findMyMatchingRequests = matchingRequestQueryRepository.findByParticipant(participant);
        List<FindMyMatchingReqData> convertedMyMatchingRequests = findMyMatchingRequests.stream().map(
                FindMyMatchingReqData::toFindMatchingReqData
        ).toList();
        return FindMyMatchingReqResponse.builder()
                .data(convertedMyMatchingRequests)
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
        findMatchingPost.getMatchingRequests()
                .forEach(matchingRequest -> {
                    if(matchingRequest.getId().equals(matchingRequestId)){
                        matchingRequest.acceptMatchingRequest();
                        // accept alarm generation
                        Alarm matchingRequestAccept = Alarm.builder()
                                .matchingPost(findMatchingPost)
                                .owner(matchingRequest.getRepresentative())
                                .alarmType(AlarmType.ACCEPT)
                                .build();
                        alarmRepository.save(matchingRequestAccept);
                    }else{
                        matchingRequest.rejectMatchingRequest();
                        // reject alarm generation
                        // alarm 생성
                        Alarm matchingRequestReject = Alarm.builder()
                                .matchingPost(findMatchingPost)
                                .owner(matchingRequest.getRepresentative())
                                .alarmType(AlarmType.REJECT)
                                .build();
                        alarmRepository.save(matchingRequestReject);
                    }
                });

        // return value
        return AcceptMatchingRequestResponse.builder()
                .matchingRequestId(matchingRequestId)
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
        // alarm 생성
        Alarm matchingRequestReject = Alarm.builder()
                .matchingPost(findMatchingPost)
                .owner(findMatchingRequest.getRepresentative())
                .alarmType(AlarmType.REJECT)
                .build();
        alarmRepository.save(matchingRequestReject);
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
}
