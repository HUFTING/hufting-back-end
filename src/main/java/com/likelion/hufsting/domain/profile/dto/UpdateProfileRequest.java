package com.likelion.hufsting.domain.profile.dto;


import com.likelion.hufsting.global.validation.EnumFormat;
import com.likelion.hufsting.domain.profile.domain.Mbti;
import com.likelion.hufsting.global.domain.Gender;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateProfileRequest {
    @EnumFormat(enumClass = Gender.class)
    private Gender gender;
    @Pattern(regexp = "[0-9]{2}학번")
    private String studentNumber;
    @EnumFormat(enumClass = Mbti.class)
    private Mbti mbti;
    private LocalDate birthday;
    private String content;
}
