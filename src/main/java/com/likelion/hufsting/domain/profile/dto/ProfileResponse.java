package com.likelion.hufsting.domain.profile.dto;

import com.likelion.hufsting.domain.profile.domain.Profile;
import lombok.Getter;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

@Getter
public class ProfileResponse {
    private String gender;
    private String studentNumber;
    private String mbti;
    private LocalDate birthday;
    private String content;

    public ProfileResponse(Profile profile) {
        this.gender = profile.getGender();
        this.studentNumber = profile.getStudentNumber();
        this.mbti = profile.getMbti();
        this.birthday = profile.getBirthday();
        this.content = profile.getContent();
    }
}
