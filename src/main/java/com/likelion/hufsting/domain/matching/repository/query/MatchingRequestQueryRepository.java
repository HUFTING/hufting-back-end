package com.likelion.hufsting.domain.matching.repository.query;

import com.likelion.hufsting.domain.matching.domain.MatchingRequest;
import com.likelion.hufsting.domain.oauth.domain.Member;
import com.likelion.hufsting.domain.profile.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MatchingRequestQueryRepository {
    private final EntityManager em; // 엔티티 매니저

    // 내 매칭 참여 현황 가져오기
    public List<MatchingRequest> findByParticipant(Member participant){
        Long participantId = 153L; // 임시 사용자 ID
        String jpql = "select distinct mr from MatchingRequest mr"
                + " join fetch mr.matchingPost mrmp"
                + " join fetch mr.representative mrr"
                + " join mr.participants mrp"
                + " join mrp.participant mrpp"
                + " where mrpp.id = :participantId";
        TypedQuery<MatchingRequest> query = em.createQuery(jpql, MatchingRequest.class);
        query.setParameter("participantId", participantId);
        return query.getResultList();
    }
}
