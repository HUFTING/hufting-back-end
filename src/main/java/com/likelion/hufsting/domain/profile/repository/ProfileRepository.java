package com.likelion.hufsting.domain.profile.repository;

import com.likelion.hufsting.domain.profile.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile,Long> {
}
