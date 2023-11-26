package com.likelion.hufsting.profile.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "studentNumber", nullable = false)
    private String studentNumber;

    @Column(name = "major", nullable = false)
    private String major;

    @Column(name = "mbti", nullable = false)
    private String mbti;

    @Column(name = "content", nullable = false)
    private String content;

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
