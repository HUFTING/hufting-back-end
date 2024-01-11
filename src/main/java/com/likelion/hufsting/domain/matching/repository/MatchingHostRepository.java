package com.likelion.hufsting.domain.matching.repository;

import com.likelion.hufsting.domain.matching.domain.MatchingHost;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MatchingHostRepository {
    private final EntityManager em; // EntityManager

    public void save(MatchingHost matchingHost){
        em.persist(matchingHost);
    }
}
