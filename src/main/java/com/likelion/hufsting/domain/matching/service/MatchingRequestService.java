package com.likelion.hufsting.domain.matching.service;

import com.likelion.hufsting.domain.matching.domain.*;
import com.likelion.hufsting.domain.matching.dto.CreateMatchingReqData;
import com.likelion.hufsting.domain.matching.dto.CreateMatchingReqResponse;
import com.likelion.hufsting.domain.matching.dto.UpdateMatchingReqData;
import com.likelion.hufsting.domain.matching.repository.MatchingPostRepository;
import com.likelion.hufsting.domain.matching.repository.MatchingRequestRepository;
import com.likelion.hufsting.domain.profile.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    // 매칭 신청 생성
    @Transactional
    public CreateMatchingReqResponse createMatchingRequests(CreateMatchingReqData dto){
        Member representative = new Member(); // 임시 대표 신청자
        MatchingPost matchingPost = matchingPostRepository.findById(dto.getMatchingPostId())
                .orElseThrow(() -> new IllegalArgumentException("Not Found: " + dto.getMatchingPostId()));
        MatchingRequest newMatchingRequest = MatchingRequest.builder()
                .matchingPost(matchingPost)
                .representative(representative)
                .participants(new ArrayList<>())
                .matchingAcceptance(MatchingAcceptance.WAITING)
                .build();
        // Member 리스트 가져오기
        List<MatchingParticipant> matchingParticipants = createMatchingParticipantsById(newMatchingRequest, dto.getParticipantIds());
        newMatchingRequest.addParticipant(matchingParticipants);
        matchingRequestRepository.save(newMatchingRequest);
        return new CreateMatchingReqResponse(dto.getParticipantIds());
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
    public Long updateMatchingRequest(Long matchingRequestId, UpdateMatchingReqData dto){
        // matchingRequest 조회
        MatchingRequest matchingRequest = matchingRequestRepository.findById(matchingRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found: " + matchingRequestId));
        // 매칭 신청 수정
        matchingRequest.updateParticipant(
                createMatchingParticipantsById(matchingRequest, dto.getIds())
        );
        return matchingRequestId;
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
