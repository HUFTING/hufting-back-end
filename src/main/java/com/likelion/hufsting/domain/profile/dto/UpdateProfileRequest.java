package com.likelion.hufsting.domain.profile.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateProfileRequest {
    private String gender;
    private String studentNumber;
    private String mbti;
    private LocalDate birthday;
    private String content;
}
