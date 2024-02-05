package com.likelion.hufsting.domain.profile.dto;

import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.domain.profile.domain.Profile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddProfileRequest {
    private String gender;
    private String studentNumber;
    private String mbti;
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
