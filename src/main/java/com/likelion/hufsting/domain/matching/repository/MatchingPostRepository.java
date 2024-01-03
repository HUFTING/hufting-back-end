package com.likelion.hufsting.domain.matching.repository;

import com.likelion.hufsting.domain.matching.domain.MatchingPost;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MatchingPostRepository {
    @PersistenceContext
    private EntityManager em; // entityManager

    public Long save(MatchingPost matchingPost){
        em.persist(matchingPost);
        return matchingPost.getId();
    }

    public MatchingPost findById(Long id){
        return em.find(MatchingPost.class, id);
    }

    /*
    public List<MatchingPost> findAll(){
    }
    */
}
