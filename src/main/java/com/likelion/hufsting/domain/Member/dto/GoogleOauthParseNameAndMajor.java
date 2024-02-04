package com.likelion.hufsting.domain.Member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GoogleOauthParseNameAndMajor {
    private String name;
    private String major;
}
