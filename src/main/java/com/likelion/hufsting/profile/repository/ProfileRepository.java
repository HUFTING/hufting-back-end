package com.likelion.hufsting.profile.repository;

import com.likelion.hufsting.profile.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile,Long> {
}
