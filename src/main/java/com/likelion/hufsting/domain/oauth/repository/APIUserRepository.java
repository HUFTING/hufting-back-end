package com.likelion.hufsting.domain.oauth.repository;


import com.likelion.hufsting.domain.oauth.domain.APIUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface APIUserRepository extends JpaRepository<APIUser, String> {

    Optional<APIUser> findByMid(String mid);
}
