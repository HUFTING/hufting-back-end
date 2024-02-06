package com.likelion.hufsting.domain.matching.repository;

import com.likelion.hufsting.domain.matching.domain.MatchingPost;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface MatchingPostRepository extends JpaRepository<MatchingPost, Long> {
    List<MatchingPost> findByTitleContaining(String input);
}
