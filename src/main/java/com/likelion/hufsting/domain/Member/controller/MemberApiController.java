package com.likelion.hufsting.domain.Member.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.likelion.hufsting.domain.Member.dto.GoogleOauthLoginResponse;
import com.likelion.hufsting.domain.Member.exception.EmailDomainException;
import com.likelion.hufsting.domain.Member.service.GoogleOauthService;
import com.likelion.hufsting.domain.Member.service.MemberService;
import com.likelion.hufsting.global.dto.ErrorResponse;
import com.likelion.hufsting.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberApiController {
    private final GoogleOauthService oauthService;
    private final MemberService memberService;
    // constant
    private final String JSON_PARSING_ERR_KEY = "jsonParsing";
    private final String EMAIL_DOMAIN_ERR_KEY = "emailDomain";

    @GetMapping("/auth/google/callback")
    public ResponseEntity<ResponseDto> googleLoginCallbackRequest(@RequestParam("code") String code){
        try {
            log.info("Request to login google oauth2");
            GoogleOauthLoginResponse responseBody = oauthService.googleLogin(code);
            // cookie setting
            ResponseCookie responseCookie = ResponseCookie.from("access_token", responseBody.getAccessToken())
                    .httpOnly(true)
                    .maxAge(60*60)
                    .path("/")
                    .build();
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                    .body(responseBody);
        }catch (JsonProcessingException e){
            ErrorResponse errorResponse = ErrorResponse.createSingleResponseErrorMessage(
                    JSON_PARSING_ERR_KEY,
                    e.getMessage()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (EmailDomainException e){
            ErrorResponse errorResponse = ErrorResponse.createSingleResponseErrorMessage(
                    EMAIL_DOMAIN_ERR_KEY,
                    e.getMessage()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/api/v1/member")
    public ResponseEntity<ResponseDto> deleteMember(Authentication authentication){
        try{
            log.info("Request to delete member");
            memberService.removeMember(authentication);
            // cookie setting
            ResponseCookie responseCookie = ResponseCookie.from("access_token", "")
                    .httpOnly(true)
                    .maxAge(0)
                    .path("/")
                    .build();
            return ResponseEntity.noContent()
                    .header(HttpHeaders.SET_COOKIE, responseCookie.toString()).build();
        }catch (IllegalArgumentException e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
