package com.likelion.hufsting.domain.Member.dto;

import com.likelion.hufsting.domain.Member.domain.ProfileSetUp;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoogleLoginResponse {
    private String email;
    private String name;
    private String major;
    private ProfileSetUp profileSetUp;
    private String accessToken;
}
