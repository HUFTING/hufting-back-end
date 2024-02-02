package com.likelion.hufsting.domain.matching.service;

import com.likelion.hufsting.domain.matching.domain.*;
import com.likelion.hufsting.domain.matching.dto.matchingrequest.*;
import com.likelion.hufsting.domain.matching.repository.MatchingPostRepository;
import com.likelion.hufsting.domain.matching.repository.MatchingRequestRepository;
import com.likelion.hufsting.domain.matching.repository.query.MatchingRequestQueryRepository;

import com.likelion.hufsting.domain.matching.validation.MatchingPostMethodValidator;
import com.likelion.hufsting.domain.matching.validation.MatchingReqMethodValidator;
import com.likelion.hufsting.domain.Member.domain.Member;
import lombok.RequiredArgsConstructor;
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
    // Validators
    private final MatchingPostMethodValidator matchingPostMethodValidator;
    private final MatchingReqMethodValidator matchingReqMethodValidator;

    // 매칭 신청 생성
    @Transactional
    public CreateMatchingReqResponse createMatchingRequests(CreateMatchingReqData dto){
        Member representative = Member.builder().build(); // 임시 대표 신청자
        // get matchingPost
        MatchingPost matchingPost = matchingPostRepository.findById(dto.getMatchingPostId())
                .orElseThrow(() -> new IllegalArgumentException("Not Found: " + dto.getMatchingPostId()));
        // validation-1 : DTO
        matchingReqMethodValidator.validateParticipantsField(
                dto.getParticipantIds(),
                1L,
                matchingPost.getMatchingHosts().size()
        );
        // validation-2 : MatchingPost
        matchingPostMethodValidator.validateMatchingPostStatus(
                matchingPost.getMatchingStatus()
        );
        // create matching request obj
        MatchingRequest newMatchingRequest = MatchingRequest.builder()
                .matchingPost(matchingPost)
                .representative(representative)
                .matchingAcceptance(MatchingAcceptance.WAITING)
                .build();
        // Member 리스트 가져오기
        List<MatchingParticipant> matchingParticipants = createMatchingParticipantsById(newMatchingRequest, dto.getParticipantIds());
        newMatchingRequest.addParticipant(matchingParticipants);
        matchingRequestRepository.save(newMatchingRequest);

        // return value generation
        Long createdMatchingRequestId = newMatchingRequest.getId();
        List<Long> createdMatchingRequestParticipants = dto.getParticipantIds();
        return new CreateMatchingReqResponse(createdMatchingRequestId, createdMatchingRequestParticipants);
    }

    // 매칭 신청 취소
    @Transactional
    public void removeMatchingRequest(Long matchingRequestId){
        MatchingRequest matchingRequest = matchingRequestRepository.findById(matchingRequestId)
                        .orElseThrow(() -> new IllegalArgumentException("Not Found: " + matchingRequestId));
        matchingReqMethodValidator.validateCanBeDeleted(matchingRequest.getMatchingAcceptance());
        matchingRequestRepository.delete(matchingRequest);
    }

    // 매칭 신청 수정
    @Transactional
    public UpdateMatchingReqResponse updateMatchingRequest(Long matchingRequestId, UpdateMatchingReqData dto){
        // matchingRequest 조회
        MatchingRequest matchingRequest = matchingRequestRepository.findById(matchingRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found: " + matchingRequestId));
        // matchingPost 조회
        MatchingPost matchingPost = matchingPostRepository.findById(dto.getMatchingPostId())
                .orElseThrow(() -> new IllegalArgumentException("Not Found: " + dto.getMatchingPostId()));
        // validation-1 : DTO
        matchingReqMethodValidator.validateParticipantsField(
                dto.getParticipantIds(),
                1L,
                matchingPost.getMatchingHosts().size()
        );
        // validation-2 : MatchingRequest
        matchingReqMethodValidator.validateCanBeModified(
                matchingRequest.getMatchingAcceptance()
        );
        // 매칭 신청 수정
        matchingRequest.updateParticipant(
                createMatchingParticipantsById(matchingRequest, dto.getParticipantIds())
        );

        return new UpdateMatchingReqResponse(matchingRequestId, dto.getParticipantIds());
    }

    // 내 매칭 신청 현황 확인
    public FindMyMatchingReqResponse findMyMatchingRequest(){
        Member participant = Member.builder().build(); // 임시 인증 유저
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
    public AcceptMatchingRequestResponse acceptMatchingRequest(Long matchingRequestId){
        MatchingRequest findMatchingRequest = matchingRequestRepository.findById(matchingRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found: " + matchingRequestId));
        MatchingPost findMatchingPost = findMatchingRequest.getMatchingPost();
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
                    }else{
                        matchingRequest.rejectMatchingRequest();
                    }
                });
        return AcceptMatchingRequestResponse.builder()
                .matchingRequestId(matchingRequestId)
                .build();
    }

    // 매칭 거부
    @Transactional
    public RejectMatchingRequestResponse rejectMatchingRequest(Long matchingRequestId){
        MatchingRequest findMatchingRequest = matchingRequestRepository.findById(matchingRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found: " + matchingRequestId));
        // validation : matchingRequest
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
    private List<MatchingParticipant> createMatchingParticipantsById(MatchingRequest matchingRequest,List<Long> participantIds){
        List<MatchingParticipant> matchingParticipants = new ArrayList<>();
        for(Long participantId : participantIds){
            Member findParticipant = Member.builder().build(); // 임시 사용자 생성
            matchingParticipants.add(new MatchingParticipant(matchingRequest, findParticipant));
        }
        return matchingParticipants;
    }
}
