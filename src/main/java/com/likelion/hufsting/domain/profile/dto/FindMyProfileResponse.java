package com.likelion.hufsting.domain.profile.dto;

import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.domain.profile.domain.Mbti;
import com.likelion.hufsting.domain.profile.domain.Profile;
import com.likelion.hufsting.global.domain.Gender;
import com.likelion.hufsting.global.dto.ResponseDto;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class FindMyProfileResponse implements ResponseDto {
    private final Long id;
    private final String name;
    private final String major;
    private final Gender gender;
    private final String studentNumber;
    private final Mbti mbti;
    private final String age;
    private final String content;
    private final String profile;

    public FindMyProfileResponse(Member member, Profile profile){
        this.id = member.getId();
        this.name = member.getName();
        this.major = member.getMajor();
        this.gender = profile.getGender();
        this.studentNumber = profile.getStudentNumber();
        this.mbti = profile.getMbti();
        this.age = profile.getAge();
        this.content = profile.getContent();
        this.profile = member.getPhotoUrl();
    }
}
