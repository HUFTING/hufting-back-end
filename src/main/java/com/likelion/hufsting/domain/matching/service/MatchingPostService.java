package com.likelion.hufsting.domain.matching.service;

import com.likelion.hufsting.domain.matching.domain.MatchingHost;
import com.likelion.hufsting.domain.matching.domain.MatchingPost;
import com.likelion.hufsting.domain.matching.domain.MatchingStatus;
import com.likelion.hufsting.domain.matching.dto.CreateMatchingPostData;
import com.likelion.hufsting.domain.matching.dto.UpdateMatchingPostData;
import com.likelion.hufsting.domain.matching.repository.MatchingPostRepository;
import com.likelion.hufsting.domain.profile.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
        return matchingPostRepository.findById(matchingPostId);
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
        List<MatchingHost> matchingHosts = new ArrayList<>();
        matchingHosts.add(new MatchingHost(matchingPost, author)); // 작성자 추가
        for(int hostId : dto.getParticipants()){
            Member findHost = new Member(); // 임시 사용자 생성
            matchingHosts.add(new MatchingHost(matchingPost, findHost));
        }
        // 매칭 글에 참가자(* 호스트) 추가
        matchingPost.addHost(matchingHosts);
        // 매칭글 영속화
        matchingPostRepository.save(matchingPost);
        return matchingPost.getId();
    }
    // 훕팅 글 수정
    @Transactional
    public Long updateMatchingPost(Long matchingPostId, UpdateMatchingPostData updateMatchingPostData){
        MatchingPost matchingPost = matchingPostRepository.findById(matchingPostId);
        matchingPost.matchingPostUpdate(updateMatchingPostData); // 변경 감지(Dirty checking)
        return matchingPostId;
    }
    // 훕팅 글 삭제
    @Transactional
    public void removeMatchingPost(Long matchingPostId){
        matchingPostRepository.delete(matchingPostId);
    }
}
