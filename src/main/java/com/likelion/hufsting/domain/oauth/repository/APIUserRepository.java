package com.likelion.hufsting.domain.oauth.repository;


import com.likelion.hufsting.domain.oauth.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface APIUserRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);
}
