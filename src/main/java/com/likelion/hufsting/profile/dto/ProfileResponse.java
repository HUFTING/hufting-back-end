package com.likelion.hufsting.profile.dto;

import com.likelion.hufsting.profile.domain.Profile;
import lombok.Getter;

@Getter
public class ProfileResponse {
    private String name;
    private String gender;
    private String studentNumber;
    private String major;
    private String mbti;
    private String content;

    public ProfileResponse(Profile profile) {
        this.name = profile.getName();
        this.gender = profile.getGender();
        this.studentNumber = profile.getStudentNumber();
        this.major = profile.getMajor();
        this.mbti = profile.getMbti();
        this.content = profile.getContent();
    }
}
