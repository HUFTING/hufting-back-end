package com.likelion.hufsting.domain.matching.repository;

import com.likelion.hufsting.domain.matching.domain.MatchingHost;
import com.likelion.hufsting.domain.matching.domain.MatchingPost;
import com.likelion.hufsting.domain.profile.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MatchingPostRepository {
    private final EntityManager em; // entityManager

    public MatchingPost findById(Long id){
        return em.find(MatchingPost.class, id);
    }

    public List<MatchingPost> findAll(){
        return em.createQuery("select mp from MatchingPost mp", MatchingPost.class)
                .getResultList();
    }

    public void save(MatchingPost matchingPost){
        em.persist(matchingPost);
    }

    public void delete(Long matchingPostId){
        MatchingPost deleteMatchingPost = em.find(MatchingPost.class, matchingPostId);
        em.remove(deleteMatchingPost);
    }
}
