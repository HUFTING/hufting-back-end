package com.likelion.hufsting.domain.matching.service;

import com.likelion.hufsting.domain.matching.repository.MatchingPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatchingPostService {
    @Autowired
    private MatchingPostRepository matchingPostRepository;

    // 훕팅 글 전체 조회
    // 훕팅 글 상세 조회
    // 훕팅 글 등록
    // 훕팅 글 수정
    // 훕팅 글 삭제
}
