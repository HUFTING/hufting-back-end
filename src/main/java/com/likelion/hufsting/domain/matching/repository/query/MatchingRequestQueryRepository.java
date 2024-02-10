package com.likelion.hufsting.domain.matching.repository.query;

import com.likelion.hufsting.domain.matching.domain.MatchingRequest;
import com.likelion.hufsting.domain.Member.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class MatchingRequestQueryRepository {
    private final EntityManager em; // 엔티티 매니저

    // 내 매칭 참여 현황 가져오기
    public List<MatchingRequest> findByParticipant(Member participant){
        Long participantId = participant.getId();
        String jpql = "select distinct mr from MatchingRequest mr"
                + " join fetch mr.matchingPost mrmp"
                + " join fetch mr.representative mrr"
                + " join mr.participants mrp"
                + " join mrp.participant mrpp"
//                + " join mrpp.profile mrppp"
                + " where mrpp.id = :participantId";
        TypedQuery<MatchingRequest> query = em.createQuery(jpql, MatchingRequest.class);
        query.setParameter("participantId", participantId);
        return query.getResultList();
    }

    public Optional<MatchingRequest> findById(Long matchingRequestId){
        String jpql = "select distinct mr from MatchingRequest mr" +
                " join fetch mr.participants mrp" +
                " where mr.id = :matchingRequestId";
        TypedQuery<MatchingRequest> query = em.createQuery(jpql, MatchingRequest.class);
        query.setParameter("matchingRequestId", matchingRequestId);
        return Optional.ofNullable(query.getSingleResult());
    }

    public Optional<MatchingRequest> findByParticipantAndPostId(Long participantId, Long matchingPostId){
        String jpql = "select distinct mr from MatchingRequest mr" +
                " join mr.matchingPost mpm" +
                " join mr.participants mrp" +
                " join mrp.participant mrpp" +
                " where mrpp.id = :participantId" +
                " and mpm.id = :matchingPostId";
        TypedQuery<MatchingRequest> query = em.createQuery(jpql, MatchingRequest.class);
        query.setParameter("participantId", participantId);
        query.setParameter("matchingPostId", matchingPostId);
        try{
            MatchingRequest findMatchingRequest = query.getSingleResult();
            return Optional.of(findMatchingRequest);
        }catch (NoResultException e){
            log.info(e.getMessage());
        }
        return Optional.empty();
    }
}
