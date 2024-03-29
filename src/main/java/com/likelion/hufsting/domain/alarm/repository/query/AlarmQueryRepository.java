package com.likelion.hufsting.domain.alarm.repository.query;

import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.domain.alarm.domain.Alarm;
import com.likelion.hufsting.domain.alarm.domain.AlarmType;
import com.likelion.hufsting.domain.matching.domain.MatchingPost;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class AlarmQueryRepository {
    private final EntityManager em;

    public List<Alarm> findByOwner(Member owner){
        Long ownerId = owner.getId();
        String jpql = "select distinct a from Alarm a" +
                " join fetch a.matchingPost amp" +
                " join fetch a.owner ao" +
                " where ao.id = :ownerId" +
                " order by a.createdAt desc";
        TypedQuery<Alarm> query = em.createQuery(jpql, Alarm.class)
                .setParameter("ownerId", ownerId);
        return query.getResultList();
    }

//    public Optional<Alarm> findMyAcceptAlarm(Member owner, MatchingPost matchingPost){
//        Long ownerId = owner.getId();
//        Long matchingPostId = matchingPost.getId();
//        String jpql = "select a from Alarm a" +
//                " join a.owner ao" +
//                " join a.matchingPost amp" +
//                " where ao.id = :ownerId" +
//                " and amp.id = :matchingPostId" +
//                " and a.alarmType = :alarmType";
//        TypedQuery<Alarm> query = em.createQuery(jpql, Alarm.class)
//                .setParameter("ownerId", ownerId)
//                .setParameter("matchingPostId", matchingPostId)
//                .setParameter("alarmType", AlarmType.ACCEPT);
//        try{
//           Alarm alarm = query.getSingleResult();
//           return Optional.of(alarm);
//        }catch (Exception e){
//            log.error(e.getMessage());
//            System.out.println("?????!!!!!!!!!!여긴가");
//        }
//        return Optional.empty();
//    }
}
