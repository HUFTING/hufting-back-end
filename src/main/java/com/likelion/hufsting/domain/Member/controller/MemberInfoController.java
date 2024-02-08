package com.likelion.hufsting.domain.Member.controller;

import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.domain.Member.dto.MemberDetailInfoResponse;
import com.likelion.hufsting.domain.Member.dto.MemberInfoResponse;
import com.likelion.hufsting.domain.Member.service.MemberInfoService;
import java.util.List;

import com.likelion.hufsting.global.dto.ErrorResponse;
import com.likelion.hufsting.global.dto.ResponseDto;
import com.likelion.hufsting.global.exception.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RestController
public class MemberInfoController {
    private final String NOT_FOUND_FOLLOWEE_ERR_KEY = "follow";
    private final MemberInfoService memberInfoService;
    @GetMapping("/api/v1/searching")
    public ResponseEntity<MemberInfoResponse> follow(@RequestParam("member_email") String memberEmail) {
        MemberInfoResponse memberInfoResponse = memberInfoService.findByEmail(memberEmail);
        return ResponseEntity.ok(memberInfoResponse);
    }

    @GetMapping("/api/v1/followingList")
    public ResponseEntity<List<MemberInfoResponse>> findAllFollowing(Authentication authentication) {
        System.out.println(authentication.getName());
        List<MemberInfoResponse> followList = memberInfoService.getFollowerList(authentication);
        return ResponseEntity.ok(followList);
    }

    @GetMapping("/api/v1/member/{memberId}")
    public ResponseEntity<ResponseDto> getMemberDetailInfoById(@PathVariable Long memberId, Authentication authentication){
        try {
            log.info("Request to get member info {}", memberId);
            MemberDetailInfoResponse response = memberInfoService.findMemberDetailInfoById(memberId, authentication);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (IllegalArgumentException e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (AuthException e){
            log.error(e.getMessage());
            ErrorResponse errorResponse = ErrorResponse.createSingleResponseErrorMessage(
                    NOT_FOUND_FOLLOWEE_ERR_KEY,
                    e.getMessage()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
    }
}
