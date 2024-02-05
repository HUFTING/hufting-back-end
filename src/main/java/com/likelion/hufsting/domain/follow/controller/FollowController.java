package com.likelion.hufsting.domain.follow.controller;

import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.domain.Member.exception.MemberRequestException;
import com.likelion.hufsting.domain.follow.service.FollowService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class FollowController {

    private final FollowService followService;

    @PostMapping("/api/v1/follow/{userId}")
    public Boolean followMember(@PathVariable Long memberId, Authentication authentication) {

        return followService.toggleMember(memberId, authentication);
    }

}
