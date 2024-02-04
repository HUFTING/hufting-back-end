package com.likelion.hufsting.domain.Member.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GoogleOauthUserInfo {
    private String name;
    private String picture;
    private String email;
}
