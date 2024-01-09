package com.likelion.hufsting.domain.matching.service;

import com.likelion.hufsting.domain.matching.domain.MatchingPost;
import com.likelion.hufsting.domain.matching.repository.MatchingPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Long saveMatchingPost(MatchingPost matchingPost){
        matchingPostRepository.save(matchingPost);
        return matchingPost.getId();
    }
    // 훕팅 글 수정
    // 훕팅 글 삭제
    @Transactional
    public Long removeMatchingPost(Long matchingPostId){
        matchingPostRepository.delete(matchingPostId);
        return matchingPostId;
    }
}
