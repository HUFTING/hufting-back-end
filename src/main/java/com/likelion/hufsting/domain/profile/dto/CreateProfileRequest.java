package com.likelion.hufsting.domain.profile.dto;

import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.domain.profile.domain.Mbti;
import com.likelion.hufsting.domain.profile.validation.BirthdayRange;
import com.likelion.hufsting.global.domain.Gender;
import com.likelion.hufsting.global.validation.EnumFormat;
import com.likelion.hufsting.domain.profile.domain.Profile;
import com.likelion.hufsting.global.validation.NullPossibleEnumFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateProfileRequest {
    private final int currentYear = LocalDate.now().getYear();

    @EnumFormat(enumClass = Gender.class)
    private Gender gender;
    @Pattern(regexp = "[0-9]{2}학번", message = "올바른 학번을 입력해주세요.")
    private String studentNumber;
    @NullPossibleEnumFormat(enumClass = Mbti.class)
    private Mbti mbti;
    @BirthdayRange
    private int birthday;
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
