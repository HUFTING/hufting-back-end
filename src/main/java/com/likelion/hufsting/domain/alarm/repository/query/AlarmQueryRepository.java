package com.likelion.hufsting.domain.alarm.repository.query;

import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.domain.alarm.domain.Alarm;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AlarmQueryRepository {
    private final EntityManager em;

    public List<Alarm> findByOwner(Member owner){
        Long ownerId = owner.getId();
        String jpql = "select distinct a from Alarm a" +
                " join fetch a.matchingPost amp" +
                " join fetch a.owner ao" +
                " where ao.id = :ownerId";
        TypedQuery<Alarm> query = em.createQuery(jpql, Alarm.class)
                .setParameter("ownerId", ownerId);
        return query.getResultList();
    }
}
