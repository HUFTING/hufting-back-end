package com.likelion.hufsting.domain.Member.dto;

import com.likelion.hufsting.global.dto.ResponseDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoogleOauthLoginResponse implements ResponseDto {
    private String email;
    private String name;
    private String major;
    private Boolean profileSetUpStatus;
    private String accessToken;
}
