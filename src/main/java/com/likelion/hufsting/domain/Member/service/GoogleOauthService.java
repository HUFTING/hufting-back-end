package com.likelion.hufsting.domain.Member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.domain.Member.domain.ProfileSetUp;
import com.likelion.hufsting.domain.Member.domain.Role;
import com.likelion.hufsting.domain.Member.dto.GoogleLoginResponse;
import com.likelion.hufsting.domain.Member.dto.GoogleOauthToken;
import com.likelion.hufsting.domain.Member.dto.GoogleUserInfo;
import com.likelion.hufsting.domain.Member.repository.MemberRepository;
import com.likelion.hufsting.domain.Member.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GoogleOauthService {
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final String PARAM_CODE = "code";
    private final String PARAM_CLIENT_ID = "client_id";
    private final String PARAM_SECRET_KEY = "client_secret";
    private final String PARAM_REDIRECT_URI = "redirect_uri";
    private final String PARAM_GRANT_TYPE = "grant_type";
    private final String GOOGLE_TOKEN_REQ_URL = "https://oauth2.googleapis.com/token";
    private final String GOOGLE_USER_INFO_URL = "https://www.googleapis.com/oauth2/v3/userinfo";

    @Transactional
    public GoogleLoginResponse googleLogin(String code) throws JsonProcessingException{
        // get data from google
        ResponseEntity<String> accessTokenResponse = requestAccessToken(code);
        GoogleOauthToken oauthToken = getAccessToken(accessTokenResponse);
        ResponseEntity<String> userInfoResponse = requestUserInfo(oauthToken);
        GoogleUserInfo userInfo = getUserInfo(userInfoResponse);
        // find user or save user
        Member member = memberRepository.findByEmail(userInfo.getEmail()).orElseGet(() -> {
            Member newMember = Member.builder()
                    .email(userInfo.getEmail())
                    .role(Role.ROLE_USER)
                    .profileSetUp(ProfileSetUp.NOT_SETTING)
                    .build();
            memberRepository.save(newMember);
            return newMember;
        });
        String accessToken = jwtUtil.generateAccessToken(member);
        return GoogleLoginResponse.builder()
                .email(member.getEmail())
                .accessToken(accessToken)
                .profileSetUp(member.getProfileSetUp())
                .build();
    }

    private GoogleUserInfo getUserInfo(ResponseEntity<String> response) throws JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        return om.readValue(response.getBody(), GoogleUserInfo.class);
    }

    private ResponseEntity<String> requestUserInfo(GoogleOauthToken googleOauthToken){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + googleOauthToken.getAccess_token());
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(GOOGLE_USER_INFO_URL, HttpMethod.GET, request, String.class);
        if(responseEntity.getStatusCode() == HttpStatus.OK){
            return responseEntity;
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private GoogleOauthToken getAccessToken(ResponseEntity<String> response) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        return om.readValue(response.getBody(), GoogleOauthToken.class);
    }

    private ResponseEntity<String> requestAccessToken(String code){
        // params setting
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", "753499212823-ftql7ed4h06gcbm8rrtrbmgg0h5odttl.apps.googleusercontent.com");
        params.add("client_secret", "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");
        params.add("redirect_uri", "http://localhost:8080/auth/google/callback");
        params.add("grant_type", "authorization_code");

        // http headers setting
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // http entity setting
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        // request token to resource server
        ResponseEntity<String> responseEntity = restTemplate.exchange(GOOGLE_TOKEN_REQ_URL, HttpMethod.POST, request, String.class);
        // check response ok
        if(responseEntity.getStatusCode() == HttpStatus.OK){
            return responseEntity;
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
