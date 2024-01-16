package com.likelion.hufsting.domain.matching.service;

import com.likelion.hufsting.domain.matching.domain.MatchingPost;
import com.likelion.hufsting.domain.matching.domain.MatchingRequest;
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
        MatchingPost matchingPost = matchingPostRepository.findById(dto.getMatchingPostId());
        // Member 리스트 가져오기
        for(Long participantId : dto.getParticipantIds()){
            Member participant = new Member(); // 임시 사용자
            MatchingRequest newMatchingRequest = MatchingRequest.builder()
                    .matchingPost(matchingPost)
                    .participant(participant)
                    .build();
            // 영속화(persist)
            matchingRequestRepository.save(newMatchingRequest);
        }
    }
}
