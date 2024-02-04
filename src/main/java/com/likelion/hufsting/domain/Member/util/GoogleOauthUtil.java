package com.likelion.hufsting.domain.Member.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.hufsting.domain.Member.dto.GoogleOauthToken;
import com.likelion.hufsting.domain.Member.dto.GoogleOauthUserInfo;
import com.likelion.hufsting.domain.Member.dto.GoogleOauthParseNameAndMajor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class GoogleOauthUtil {
    // constant
    private final String PARAM_CODE = "code";
    private final String PARAM_CLIENT_ID = "client_id";
    private final String PARAM_SECRET_KEY = "client_secret";
    private final String PARAM_REDIRECT_URI = "redirect_uri";
    private final String PARAM_GRANT_TYPE = "grant_type";
    private final String GOOGLE_TOKEN_REQ_URL = "https://oauth2.googleapis.com/token";
    private final String GOOGLE_USER_INFO_URL = "https://www.googleapis.com/oauth2/v3/userinfo";
    private final String PARSE_NAME_AND_MAJOR_REGEX = "\\[|/|\\]";
    private final int PARSE_NAME_INDEX = 0;
    private final int PARSE_MAJOR_INDEX = 2;
    public GoogleOauthUserInfo getUserInfo(ResponseEntity<String> response) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        return om.readValue(response.getBody(), GoogleOauthUserInfo.class);
    }

    public ResponseEntity<String> requestUserInfo(GoogleOauthToken googleOauthToken){
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

    public GoogleOauthToken getAccessToken(ResponseEntity<String> response) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        return om.readValue(response.getBody(), GoogleOauthToken.class);
    }

    public ResponseEntity<String> requestAccessToken(String code){
        // params setting
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(PARAM_CODE, code);
        params.add(PARAM_CLIENT_ID, "753499212823-ftql7ed4h06gcbm8rrtrbmgg0h5odttl.apps.googleusercontent.com");
        params.add(PARAM_SECRET_KEY, "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");
        params.add(PARAM_REDIRECT_URI, "http://localhost:8080/auth/google/callback");
        params.add(PARAM_GRANT_TYPE, "authorization_code");

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

    public GoogleOauthParseNameAndMajor parseNameAndMajor(String googleName){
        // splitting googleName
        List<String> splitResult = Arrays.asList(googleName.split(PARSE_NAME_AND_MAJOR_REGEX));
        // parsing
        String parsedName = splitResult.get(PARSE_NAME_INDEX)
                .replace(" ", "")
                .replaceAll("[\u200B-\u200D\uFEFF]", ""); // ZWC 제거
        String parsedMajor = splitResult.get(PARSE_MAJOR_INDEX).replace(" ", "");
        return new GoogleOauthParseNameAndMajor(
                parsedName, parsedMajor
        );
    }
}
