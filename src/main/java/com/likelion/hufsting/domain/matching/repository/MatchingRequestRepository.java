package com.likelion.hufsting.domain.matching.repository;

import com.likelion.hufsting.domain.matching.domain.MatchingRequest;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MatchingRequestRepository {
    private final EntityManager em;

    public void save(MatchingRequest matchingRequest){
        em.persist(matchingRequest);
    }
}
