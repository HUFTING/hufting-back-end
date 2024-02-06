package com.likelion.hufsting.domain.matching.repository;

import com.likelion.hufsting.domain.matching.domain.MatchingPost;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MatchingPostRepository extends JpaRepository<MatchingPost, Long> {
}
