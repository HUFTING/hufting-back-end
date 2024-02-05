package com.likelion.hufsting.domain.profile.dto;

import com.likelion.hufsting.domain.profile.domain.Mbti;
import com.likelion.hufsting.domain.profile.domain.Profile;
import com.likelion.hufsting.global.domain.Gender;
import com.likelion.hufsting.global.dto.ResponseDto;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CreateProfileResponse implements ResponseDto {
    private final Gender gender;
    private final String studentNumber;
    private final Mbti mbti;
    private final LocalDate birthday;
    private final String content;

    public CreateProfileResponse(Profile profile) {
        this.gender = profile.getGender();
        this.studentNumber = profile.getStudentNumber();
        this.mbti = profile.getMbti();
        this.birthday = profile.getBirthday();
        this.content = profile.getContent();
    }
}
