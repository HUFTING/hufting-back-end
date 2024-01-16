package com.likelion.hufsting.domain.matching.service;

import com.likelion.hufsting.domain.matching.domain.*;
import com.likelion.hufsting.domain.matching.dto.CreateMatchingReqData;
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
    public void createMatchingRequests(CreateMatchingReqData dto){
        Member representative = new Member(); // 임시 대표 신청자
        MatchingPost matchingPost = matchingPostRepository.findById(dto.getMatchingPostId());
        MatchingRequest newMatchingRequest = MatchingRequest.builder()
                .matchingPost(matchingPost)
                .representative(representative)
                .matchingAcceptance(MatchingAcceptance.WAITING)
                .build();
        // Member 리스트 가져오기
        newMatchingRequest.addParticipant(createMatchingParticipantsById(newMatchingRequest, dto.getParticipantIds()));
        matchingRequestRepository.save(newMatchingRequest);
    }

    // create MatchingRequests List By MemberId
    public static List<MatchingParticipant> createMatchingParticipantsById(MatchingRequest matchingRequest,List<Long> participantIds){
        List<MatchingParticipant> matchingParticipants = new ArrayList<>();
        for(Long participantId : participantIds){
            Member findHost = new Member(); // 임시 사용자 생성
            matchingParticipants.add(new MatchingParticipant(matchingRequest, findHost));
        }
        return matchingParticipants;
    }
}
