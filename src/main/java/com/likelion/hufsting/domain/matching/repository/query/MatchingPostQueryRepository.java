package com.likelion.hufsting.domain.matching.repository.query;

import com.likelion.hufsting.domain.matching.domain.MatchingPost;
import com.likelion.hufsting.domain.Member.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MatchingPostQueryRepository {
    private final EntityManager em;

    public List<MatchingPost> findByAuthor(Member host){
        Long hostId = 1L;
        String jpql = "select distinct mp from MatchingPost mp" +
                " join fetch mp.matchingRequests mpmr" +
                " join mp.matchingHosts mpmh" +
                " join mpmh.host mpmhh" +
                " where mpmhh.id = :hostId";
        TypedQuery<MatchingPost> query = em.createQuery(jpql, MatchingPost.class)
                .setParameter("hostId", hostId);
        return query.getResultList();
    }
}
