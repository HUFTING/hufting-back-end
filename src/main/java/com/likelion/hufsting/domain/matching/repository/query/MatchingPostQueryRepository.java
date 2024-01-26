package com.likelion.hufsting.domain.matching.repository.query;

import com.likelion.hufsting.domain.matching.domain.MatchingPost;
import com.likelion.hufsting.domain.oauth.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MatchingPostQueryRepository {
    private final EntityManager em;

    public List<MatchingPost> findByAuthor(Member author){
        Long authorId = 1L;
        String jpql = "select distinct mp from MatchingPost mp" +
                " join fetch mp.author mpa" +
                " where mpa.id = :authorId";
        TypedQuery<MatchingPost> query = em.createQuery(jpql, MatchingPost.class)
                .setParameter("authorId", authorId);
        return query.getResultList();
    }
}
