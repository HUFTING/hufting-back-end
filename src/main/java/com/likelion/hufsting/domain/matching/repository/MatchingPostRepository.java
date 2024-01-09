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
