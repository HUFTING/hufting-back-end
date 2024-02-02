package com.likelion.hufsting.domain.profile.domain;


import com.likelion.hufsting.domain.Member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 식별자 자동 생성
    @Column(name = "PROFILE_ID", updatable = false)
    private Long id;

    @Column(name = "PROFILE_NAME", nullable = false)
    private String name;

    @Column(name = "PROFILE_GENDER", nullable = false)
    private String gender;

    @Column(name = "PROFILE_STUDENTNUMBER", nullable = false)
    private String studentNumber;

    @Column(name = "PROFILE_MAJOR", nullable = false)
    private String major;

    @Column(name = "PROFILE_MBTI", nullable = false)
    private String mbti;

    @Column(name = "CONTENT", nullable = false)
    private String content;

    @OneToOne(mappedBy = "profile")
    private Member member;

    @Builder
    public Profile(String name, String gender, String studentNumber,
                   String major, String mbti, String content) {
        this.name = name;
        this.gender = gender;
        this.studentNumber = studentNumber;
        this.major = major;
        this.mbti = mbti;
        this.content = content;
    }

    public void update(String name, String gender, String studentNumber,
                       String major, String mbti, String content) {
        this.name = name;
        this.gender = gender;
        this.studentNumber = studentNumber;
        this.major = major;
        this.mbti = mbti;
        this.content = content;

    }

}
