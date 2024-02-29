package com.likelion.hufsting.domain.matching.repository.query;

import com.likelion.hufsting.domain.matching.domain.MatchingPost;
import com.likelion.hufsting.domain.Member.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MatchingPostQueryRepository {
    private final EntityManager em;

    public List<MatchingPost> findByAuthor(Member host){
        Long hostId = host.getId();
        String jpql = "select distinct mp from MatchingPost mp" +
                " join fetch mp.matchingHosts mpmh" +
                " join fetch mpmh.host mpmhh" +
                " where mpmhh.id = :hostId" +
                " order by mp.createdAt DESC";
        TypedQuery<MatchingPost> query = em.createQuery(jpql, MatchingPost.class)
                .setParameter("hostId", hostId);
        return query.getResultList();
    }

    public Optional<MatchingPost> findOneByAuthor(Member host, Long matchingPostId){
        Long hostId = host.getId();
        String jpql = "select distinct mp from MatchingPost mp" +
                " join fetch mp.matchingHosts mpmh" +
                " where mp.id = :matchingPostId";
        TypedQuery<MatchingPost> query = em.createQuery(jpql, MatchingPost.class)
                .setParameter("matchingPostId", matchingPostId);
        return Optional.ofNullable(query.getSingleResult());
    }
}
