package com.likelion.hufsting.domain.profile.dto;

import com.likelion.hufsting.domain.profile.domain.Mbti;
import com.likelion.hufsting.global.domain.Gender;
import com.likelion.hufsting.global.dto.ResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class UpdateProfileResponse implements ResponseDto {
    private Gender gender;
    private String studentNumber;
    private Mbti mbti;
    private int birthday;
    private String content;
}
