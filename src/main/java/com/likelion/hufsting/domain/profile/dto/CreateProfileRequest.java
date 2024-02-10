package com.likelion.hufsting.domain.profile.dto;

import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.domain.profile.domain.Mbti;
import com.likelion.hufsting.domain.profile.validation.BirthdayRange;
import com.likelion.hufsting.global.domain.Gender;
import com.likelion.hufsting.global.validation.EnumFormat;
import com.likelion.hufsting.domain.profile.domain.Profile;
import com.likelion.hufsting.global.validation.NullPossibleEnumFormat;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateProfileRequest {
    @EnumFormat(enumClass = Gender.class)
    private Gender gender;

    @Nullable
    @Pattern(regexp = "[0-9]{2}학번", message = "올바른 학번을 입력해주세요.")
    private String studentNumber;

    @NullPossibleEnumFormat(enumClass = Mbti.class)
    private Mbti mbti;

    @BirthdayRange
    private String age;

    private String content;

    public Profile toEntity(Member member) {
        return Profile.builder()
                .gender(gender)
                .studentNumber(studentNumber)
                .mbti(mbti)
                .age(age)
                .content(content)
                .member(member)
                .build();
    }
}
