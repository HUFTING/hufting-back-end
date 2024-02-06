package com.likelion.hufsting.domain.Member.controller;

import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.domain.Member.dto.MemberInfoResponse;
import com.likelion.hufsting.domain.Member.service.MemberInfoService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MemberInfoController {

    private final MemberInfoService memberInfoService;
    @GetMapping("/api/v1/searching")
    public ResponseEntity<MemberInfoResponse> follow(@RequestParam("member_email") String memberEmail) {
        MemberInfoResponse memberInfoResponse = memberInfoService.findByEmail(memberEmail);
        return ResponseEntity.ok(memberInfoResponse);
    }

    @GetMapping("/api/v1/followingList/{memberId}")
    public ResponseEntity<List<MemberInfoResponse>> findAllFollowing(@PathVariable Long memberId) {
        List<MemberInfoResponse> followList = memberInfoService.getFollowerList(memberId);
        return ResponseEntity.ok(followList);
    }
}
