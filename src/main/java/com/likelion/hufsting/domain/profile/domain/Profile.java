package com.likelion.hufsting.domain.profile.domain;


import com.likelion.hufsting.domain.Member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 식별자 자동 생성
    @Column(name = "PROFILE_ID", updatable = false)
    private Long id;

    @Column(name = "PROFILE_GENDER", nullable = false)
    private String gender;

    @Column(name = "PROFILE_STUDENTNUMBER", nullable = false)
    private String studentNumber;

    @Column(name = "PROFILE_MBTI", nullable = false)
    private String mbti;

    @Column(name = "PROFILE_BIRTHDAY", nullable = false)
    private LocalDate birthday;

    @Column(name = "PROFILE_CONTENT", nullable = false)
    private String content;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Builder
    public Profile(String gender, String studentNumber, String mbti, LocalDate birthday, String content, Member member) {
        this.gender = gender;
        this.studentNumber = studentNumber;
        this.mbti = mbti;
        this.birthday = birthday;
        this.content = content;
        this.member = member;
    }

    public void update(String gender, String studentNumber, String mbti, LocalDate birthday, String content) {
        this.gender = gender;
        this.studentNumber = studentNumber;
        this.mbti = mbti;
        this.birthday = birthday;
        this.content = content;
    }

}
