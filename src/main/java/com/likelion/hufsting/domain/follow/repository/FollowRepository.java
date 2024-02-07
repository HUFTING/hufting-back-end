package com.likelion.hufsting.domain.follow.repository;

import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.domain.follow.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow,Long> {
    List<Follow> findByFollower(Member follower);
}
