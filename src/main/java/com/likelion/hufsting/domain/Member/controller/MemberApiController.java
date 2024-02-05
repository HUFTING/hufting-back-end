package com.likelion.hufsting.domain.Member.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.likelion.hufsting.domain.Member.dto.GoogleOauthLoginResponse;
import com.likelion.hufsting.domain.Member.service.GoogleOauthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberApiController {
    private final GoogleOauthService oauthService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/auth/google/callback")
    public ResponseEntity<GoogleOauthLoginResponse> googleLoginCallbackRequest(@RequestParam("code") String code){
        try {
            log.info("Request to login google oauth2");
            GoogleOauthLoginResponse responseBody = oauthService.googleLogin(code);
            // cookie setting
            ResponseCookie responseCookie = ResponseCookie.from("access_token", responseBody.getAccessToken())
                    .httpOnly(true)
                    .maxAge(60*10)
                    .path("/")
                    .build();
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                    .body(responseBody);
        }catch (JsonProcessingException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
