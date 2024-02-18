package com.likelion.hufsting.domain.Member.repository.query;

import com.likelion.hufsting.domain.Member.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {
    private final EntityManager em;

    public Optional<Member> findById(Long memberId){
        String jpql = "select distinct m from Member m" +
                " join fetch m.profile mp" +
                " where m.id = :memberId";
        TypedQuery<Member> query = em.createQuery(jpql, Member.class)
                .setParameter("memberId", memberId);
        return Optional.ofNullable(query.getSingleResult());
    }
}
