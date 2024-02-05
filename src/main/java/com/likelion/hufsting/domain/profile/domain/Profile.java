package com.likelion.hufsting.domain.profile.domain;


import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.global.domain.Gender;
import com.likelion.hufsting.domain.profile.dto.UpdateProfileData;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Entity
@Getter
@Validated
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 식별자 자동 생성
    @Column(name = "PROFILE_ID")
    private Long id;

    @Column(name = "PROFILE_GENDER")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "PROFILE_STUDENTNUMBER")
    private String studentNumber;

    @Column(name = "PROFILE_MBTI")
    @Enumerated(EnumType.STRING)
    private Mbti mbti;

    @Column(name = "PROFILE_BIRTHDAY")
    private LocalDate birthday;

    @Column(name = "PROFILE_CONTENT")
    private String content;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Builder
    public Profile(Gender gender, String studentNumber, Mbti mbti, LocalDate birthday, String content, Member member) {
        this.gender = gender;
        this.studentNumber = studentNumber;
        this.mbti = mbti;
        this.birthday = birthday;
        this.content = content;
        this.member = member;
    }

    public void update(UpdateProfileData dto) {
        this.gender = dto.getGender();
        this.studentNumber = dto.getStudentNumber();
        this.mbti = dto.getMbti();
        this.birthday = dto.getBirthday();
        this.content = dto.getContent();
    }
}
