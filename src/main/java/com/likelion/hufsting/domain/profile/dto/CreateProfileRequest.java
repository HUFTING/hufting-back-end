package com.likelion.hufsting.domain.profile.dto;

import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.domain.profile.domain.Mbti;
import com.likelion.hufsting.global.domain.Gender;
import com.likelion.hufsting.global.validation.EnumFormat;
import com.likelion.hufsting.domain.profile.domain.Profile;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateProfileRequest {
    @EnumFormat(enumClass = Gender.class)
    private Gender gender;
    @Pattern(regexp = "[0-9]{2}학번", message = "올바른 학번을 입력해주세요.")
    private String studentNumber;
    @EnumFormat(enumClass = Mbti.class)
    private Mbti mbti;
    private LocalDate birthday;
    private String content;

    public Profile toEntity(Member member) {
        return Profile.builder()
                .gender(gender)
                .studentNumber(studentNumber)
                .mbti(mbti)
                .birthday(birthday)
                .content(content)
                .member(member)
                .build();
    }
}
