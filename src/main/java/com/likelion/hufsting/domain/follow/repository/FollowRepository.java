package com.likelion.hufsting.domain.follow.repository;

import com.likelion.hufsting.domain.follow.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow,Long> {
}
