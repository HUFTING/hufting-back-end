package com.likelion.hufsting.domain.Member.dto;

import com.likelion.hufsting.domain.profile.domain.Mbti;
import com.likelion.hufsting.global.domain.Gender;
import com.likelion.hufsting.global.dto.ResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class MemberDetailInfoResponse implements ResponseDto {
    private Long id;
    private String name;
    private String major;
    private Gender gender;
    private String studentNumber;
    private String age;
    private Mbti mbti;
    private String content;
}
