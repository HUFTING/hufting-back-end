package com.likelion.hufsting.domain.matching.repository;

import com.likelion.hufsting.domain.matching.domain.MatchingRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchingRequestRepository extends JpaRepository<MatchingRequest, Long> {
}
