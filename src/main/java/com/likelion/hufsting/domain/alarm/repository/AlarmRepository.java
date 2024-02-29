package com.likelion.hufsting.domain.alarm.repository;

import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.domain.alarm.domain.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
}
