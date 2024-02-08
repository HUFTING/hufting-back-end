package com.likelion.hufsting.domain.matching.repository.query;

import com.likelion.hufsting.domain.matching.domain.MatchingRequest;
import com.likelion.hufsting.domain.Member.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
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
                " join fetch mrp.participant mrpp" +
                " join fetch mrpp.profile mrppp" +
                " where mr.id = :matchingRequestId";
        TypedQuery<MatchingRequest> query = em.createQuery(jpql, MatchingRequest.class);
        query.setParameter("matchingRequestId", matchingRequestId);
        return Optional.ofNullable(query.getSingleResult());
    }
}
