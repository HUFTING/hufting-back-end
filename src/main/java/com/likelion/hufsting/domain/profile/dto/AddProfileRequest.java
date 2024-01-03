package com.likelion.hufsting.domain.profile.dto;

import com.likelion.hufsting.domain.profile.domain.Profile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddProfileRequest {
    private String name;
    private String gender;
    private String studentNumber;
    private String major;
    private String mbti;
    private String content;

    public Profile toEntity() {
        return Profile.builder()
                .name(name)
                .gender(gender)
                .studentNumber(studentNumber)
                .major(major)
                .mbti(mbti)
                .content(content)
                .build();
    }
}
