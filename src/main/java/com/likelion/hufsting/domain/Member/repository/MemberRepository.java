package com.likelion.hufsting.domain.Member.repository;

import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.domain.matching.domain.MatchingPost;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    //Optional<Member> findByEmailContaining(String input);
}
