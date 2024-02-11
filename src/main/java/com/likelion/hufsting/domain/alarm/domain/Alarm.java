package com.likelion.hufsting.domain.alarm.domain;

import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.domain.matching.domain.MatchingPost;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Alarm {
    @Id @GeneratedValue
    @Column(name = "ALARM_ID")
    private Long id;
    @CreationTimestamp
    private LocalDateTime createdAt; // 알림 생성일
    @Enumerated(EnumType.STRING)
    private AlarmType alarmType; // 알림 타입
    @ManyToOne(fetch = FetchType.LAZY)
    private MatchingPost matchingPost; // 알림 관련 매칭글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OWNER_ID")
    private Member owner; // 알림 주인
}
