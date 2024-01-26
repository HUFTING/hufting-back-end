package com.likelion.hufsting.domain.matching.repository;

import com.likelion.hufsting.domain.matching.domain.MatchingPost;
import com.likelion.hufsting.domain.oauth.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchingPostRepository extends JpaRepository<MatchingPost, Long> {
}
