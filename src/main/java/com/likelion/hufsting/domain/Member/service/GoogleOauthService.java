package com.likelion.hufsting.domain.Member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.domain.Member.domain.Role;
import com.likelion.hufsting.domain.Member.dto.GoogleOauthLoginResponse;
import com.likelion.hufsting.domain.Member.dto.GoogleOauthParseNameAndMajor;
import com.likelion.hufsting.domain.Member.dto.GoogleOauthToken;
import com.likelion.hufsting.domain.Member.dto.GoogleOauthUserInfo;
import com.likelion.hufsting.domain.Member.repository.MemberRepository;
import com.likelion.hufsting.domain.Member.util.GoogleOauthUtil;
import com.likelion.hufsting.domain.Member.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GoogleOauthService {
    // utils
    private final JwtUtil jwtUtil;
    private final GoogleOauthUtil googleOauthUtil;
    // repositories
    private final MemberRepository memberRepository;

    @Transactional
    public GoogleOauthLoginResponse googleLogin(String code) throws JsonProcessingException{
        // get data from google
        ResponseEntity<String> accessTokenResponse = googleOauthUtil.requestAccessToken(code);
        GoogleOauthToken oauthToken = googleOauthUtil.getAccessToken(accessTokenResponse);
        ResponseEntity<String> userInfoResponse = googleOauthUtil.requestUserInfo(oauthToken);
        GoogleOauthUserInfo userInfo = googleOauthUtil.getUserInfo(userInfoResponse);
        // parsing name, major from gotten data
        GoogleOauthParseNameAndMajor parsedResult = googleOauthUtil.parseNameAndMajor(userInfo.getName());
        System.out.println(parsedResult.getName());
        // find user or save user
        Member member = memberRepository.findByEmail(userInfo.getEmail()).orElseGet(() -> {
            Member newMember = Member.builder()
                    .email(userInfo.getEmail())
                    .name(parsedResult.getName())
                    .major(parsedResult.getMajor())
                    .role(Role.ROLE_USER)
                    .profileSetUpStatus(Boolean.FALSE)
                    .build();
            memberRepository.save(newMember);
            return newMember;
        });
        String accessToken = jwtUtil.generateAccessToken(member);
        return GoogleOauthLoginResponse.builder()
                .email(member.getEmail())
                .name(member.getName())
                .major(member.getMajor())
                .accessToken(accessToken)
                .profileSetUpStatus(Boolean.FALSE)
                .build();
    }
}
