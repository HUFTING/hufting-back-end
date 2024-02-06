package com.likelion.hufsting.domain.follow.controller;


import com.likelion.hufsting.domain.follow.service.FollowService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class FollowController {

    private final FollowService followService;

    @PostMapping("/api/v1/follow")
    public Boolean followMember(@RequestBody String memberEmail, Authentication authentication) {
        return followService.toggleMember(memberEmail, authentication);
    }

}
