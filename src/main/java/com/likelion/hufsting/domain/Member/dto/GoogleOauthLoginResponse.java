package com.likelion.hufsting.domain.Member.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoogleOauthLoginResponse {
    private String email;
    private String name;
    private String major;
    private Boolean profileSetUpStatus;
    private String accessToken;
}
