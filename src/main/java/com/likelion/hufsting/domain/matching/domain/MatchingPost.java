package com.likelion.hufsting.domain.matching.domain;

import com.likelion.hufsting.domain.profile.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "HUFSTING_POSTS")
public class MatchingPost {
    protected MatchingPost(){}
    public MatchingPost(String title, String content, Gender gender,
                        int desiredNumPeople, String openTalkLink,
                        Member author, MatchingStatus matchingStatus
                        ) {
        this.title = title;
        this.content = content;
        this.gender = gender;
        this.desiredNumPeople = desiredNumPeople;
        this.openTalkLink = openTalkLink;
        this.author = author;
        this.matchingStatus = matchingStatus;
    }

    @Id @GeneratedValue
    @Column(name = "POST_ID")
    private Long id; // PK

    private String title; // 제목
    private String content; // 내용

    @Enumerated(EnumType.STRING)
    private Gender gender; // 성별, MALE, FEMALE

    private int desiredNumPeople; // 희망 매칭 인원

    private String openTalkLink; // 오픈톡 링크

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "AUTHOR_ID")
    private Member author; // 작성자

    @Enumerated(EnumType.STRING)
    private MatchingStatus matchingStatus; // 매칭 상태, WAITING, COMPLETED

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
