package com.likelion.hufsting.domain.follow.controller;


import com.likelion.hufsting.domain.follow.dto.SuccessMessage;
import com.likelion.hufsting.domain.follow.service.FollowService;
import com.likelion.hufsting.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class FollowController {

    private final FollowService followService;

    @PostMapping("/api/v1/follow")
    public ResponseEntity<SuccessMessage> followMember(@RequestBody String memberEmail, Authentication authentication) {
        SuccessMessage successMessage = followService.toggleMember(memberEmail, authentication);
        return ResponseEntity.ok(successMessage);
    }

}
