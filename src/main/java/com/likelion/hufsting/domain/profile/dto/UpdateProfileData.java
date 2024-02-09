package com.likelion.hufsting.domain.profile.dto;

import com.likelion.hufsting.domain.profile.domain.Mbti;
import com.likelion.hufsting.global.domain.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateProfileData {
    private Gender gender;
    private String studentNumber;
    private Mbti mbti;
    private int birthday;
    private String content;

    public static UpdateProfileData toUpdateProfileData(UpdateProfileRequest dto){
        UpdateProfileData updateProfileData = new UpdateProfileData();
        updateProfileData.setGender(dto.getGender());
        updateProfileData.setStudentNumber(dto.getStudentNumber());
        updateProfileData.setMbti(dto.getMbti());
        updateProfileData.setBirthday(dto.getBirthday());
        updateProfileData.setContent(dto.getContent());
        return updateProfileData;
    }
}
