package com.likelion.hufsting.domain.matching.service;

import com.likelion.hufsting.domain.matching.domain.*;
import com.likelion.hufsting.domain.matching.dto.matchingrequest.*;
import com.likelion.hufsting.domain.matching.repository.MatchingPostRepository;
import com.likelion.hufsting.domain.matching.repository.MatchingRequestRepository;
import com.likelion.hufsting.domain.matching.repository.query.MatchingRequestQueryRepository;

import com.likelion.hufsting.domain.oauth.domain.Member;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchingRequestService {
    private final MatchingRequestRepository matchingRequestRepository;
    private final MatchingPostRepository matchingPostRepository;
    private final MatchingRequestQueryRepository matchingRequestQueryRepository;

    // 매칭 신청 생성
    @Transactional
    public CreateMatchingReqResponse createMatchingRequests(CreateMatchingReqData dto){
        Member representative = new Member(); // 임시 대표 신청자
        MatchingPost matchingPost = matchingPostRepository.findById(dto.getMatchingPostId())
                .orElseThrow(() -> new IllegalArgumentException("Not Found: " + dto.getMatchingPostId()));
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
        matchingRequestRepository.delete(matchingRequest);
    }

    // 매칭 신청 수정
    @Transactional
    public UpdateMatchingReqResponse updateMatchingRequest(Long matchingRequestId, UpdateMatchingReqData dto){
        // matchingRequest 조회
        MatchingRequest matchingRequest = matchingRequestRepository.findById(matchingRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found: " + matchingRequestId));
        // 매칭 신청 수정
        matchingRequest.updateParticipant(
                createMatchingParticipantsById(matchingRequest, dto.getParticipantIds())
        );

        return new UpdateMatchingReqResponse(matchingRequestId, dto.getParticipantIds());
    }

    // 내 매칭 신청 현황 확인
    public FindMyMatchingReqResponse findMyMatchingRequest(){
        Member participant = new Member(); // 임시 인증 유저
        List<MatchingRequest> findMyMatchingRequests = matchingRequestQueryRepository.findByParticipant(participant);
        List<FindMyMatchingReqData> convertedMyMatchingRequests = findMyMatchingRequests.stream().map(
                FindMyMatchingReqData::toFindMatchingReqData
        ).toList();
        //System.out.println(convertedMyMatchingRequests.size());
        return FindMyMatchingReqResponse.builder()
                .data(convertedMyMatchingRequests)
                .build();
    }

    // 매칭 수락
    @Transactional
    public AcceptMatchingRequestResponse acceptMatchingRequest(Long matchingRequestId){
        MatchingRequest findMatchingRequest = matchingRequestRepository.findById(matchingRequestId)
                .orElseThrow(IllegalArgumentException::new);
        MatchingPost findMatchingPost = findMatchingRequest.getMatchingPost();
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

    @Transactional
    public RejectMatchingRequestResponse rejectMatchingRequest(Long matchingRequestId){
        MatchingRequest findMatchingRequest = matchingRequestRepository.findById(matchingRequestId)
                .orElseThrow(IllegalAccessError::new);
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
            Member findParticipant = new Member(); // 임시 사용자 생성
            matchingParticipants.add(new MatchingParticipant(matchingRequest, findParticipant));
        }
        return matchingParticipants;
    }
}
