package com.likelion.hufsting.domain.matching.controller;

import com.likelion.hufsting.domain.matching.domain.MatchingPost;
import com.likelion.hufsting.domain.matching.domain.MatchingStatus;
import com.likelion.hufsting.domain.matching.dto.CreateMatchingPostRequest;
import com.likelion.hufsting.domain.matching.service.MatchingPostService;
import com.likelion.hufsting.domain.profile.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MatchingPostApiController {
    private final MatchingPostService matchingPostService;

    @PostMapping("/api/v1/matchingposts")
    public Long saveMatchingPost(@RequestBody CreateMatchingPostRequest dto){
        Member author = new Member();
        MatchingPost matchingPost = new MatchingPost(
                dto.getTitle(),
                dto.getContent(),
                dto.getGender(),
                dto.getDesiredNumPeople(),
                dto.getOpenTalkLink(),
                author, // 임시 사용자
                MatchingStatus.WAITING
        );
        return matchingPostService.saveMatchingPost(matchingPost);
    }
}
