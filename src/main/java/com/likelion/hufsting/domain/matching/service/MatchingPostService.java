package com.likelion.hufsting.domain.matching.service;

import com.likelion.hufsting.domain.matching.domain.MatchingHost;
import com.likelion.hufsting.domain.matching.domain.MatchingPost;
import com.likelion.hufsting.domain.matching.domain.MatchingStatus;
import com.likelion.hufsting.domain.matching.dto.matchingpost.CreateMatchingPostData;
import com.likelion.hufsting.domain.matching.dto.matchingpost.UpdateMatchingPostData;
import com.likelion.hufsting.domain.matching.repository.MatchingPostRepository;
import com.likelion.hufsting.domain.oauth.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchingPostService {
    private final MatchingPostRepository matchingPostRepository;

    // 훕팅 글 전체 조회
    public List<MatchingPost> findAllMatchingPost(){
        return matchingPostRepository.findAll();
    }
    // 훕팅 글 상세 조회
    public MatchingPost findByIdMatchingPost(Long matchingPostId){
        return matchingPostRepository.findById(matchingPostId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found: " + matchingPostId));
    }
    // 훕팅 글 등록
    @Transactional
    public Long saveMatchingPost(CreateMatchingPostData dto){
        // MatchingPost 생성
        Member author = new Member(); // 임시 작성자
        MatchingPost matchingPost = new MatchingPost(
                dto.getTitle(),
                dto.getContent(),
                dto.getGender(),
                dto.getDesiredNumPeople(),
                dto.getOpenTalkLink(),
                author, // 임시 사용자
                MatchingStatus.WAITING
        );
        // 호스트 조회 및 생성
        List<MatchingHost> matchingHosts = createMatchingHostsById(matchingPost, dto.getParticipants());
        matchingHosts.add(new MatchingHost(matchingPost, author)); // 작성자 추가

        // 매칭 글에 참가자(* 호스트) 추가
        matchingPost.addHost(matchingHosts);
        // 매칭글 영속화
        matchingPostRepository.save(matchingPost);
        return matchingPost.getId();
    }
    // 훕팅 글 수정
    @Transactional
    public Long updateMatchingPost(Long matchingPostId, UpdateMatchingPostData dto){
        MatchingPost matchingPost = matchingPostRepository.findById(matchingPostId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found: " + matchingPostId));
        matchingPost.updateMatchingPost(dto); // 변경 감지(Dirty checking)

        // 호스트 수정
        matchingPost.updateHost(createMatchingHostsById(matchingPost, dto.getParticipants()));
        return matchingPostId;
    }
    // 훕팅 글 삭제
    @Transactional
    public void removeMatchingPost(Long matchingPostId){
        MatchingPost matchingPost = matchingPostRepository.findById(matchingPostId)
                        .orElseThrow(() -> new IllegalArgumentException("Not Found: " + matchingPostId));
        matchingPostRepository.delete(matchingPost);
    }

    // 사용자 정의 메서드
    private List<MatchingHost> createMatchingHostsById(MatchingPost matchingPost, List<Long> hostIds){
        List<MatchingHost> matchingHosts = new ArrayList<>();
        for(Long hostId : hostIds){
            Member findHost = new Member(); // 임시 사용자 생성
            matchingHosts.add(new MatchingHost(matchingPost, findHost));
        }
        return matchingHosts;
    }
}
