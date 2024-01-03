package com.likelion.hufsting.domain.profile.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateProfileRequest {
    private String name;
    private String gender;
    private String studentNumber;
    private String major;
    private String mbti;
    private String content;
}
