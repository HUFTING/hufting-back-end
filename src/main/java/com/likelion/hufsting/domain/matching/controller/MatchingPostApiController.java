package com.likelion.hufsting.domain.matching.controller;

import com.likelion.hufsting.domain.matching.domain.MatchingPost;
import com.likelion.hufsting.domain.matching.domain.MatchingStatus;
import com.likelion.hufsting.domain.matching.dto.CreateMatchingPostRequest;
import com.likelion.hufsting.domain.matching.dto.CreateMatchingPostResponse;
import com.likelion.hufsting.domain.matching.dto.MatchingPostData;
import com.likelion.hufsting.domain.matching.dto.FindMatchingPostResponse;
import com.likelion.hufsting.domain.matching.service.MatchingPostService;
import com.likelion.hufsting.domain.profile.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MatchingPostApiController {
    private final MatchingPostService matchingPostService;

    @GetMapping("/api/v1/matchingposts")
    public FindMatchingPostResponse<MatchingPostData> getMatchingPosts(){
        List<MatchingPost> findMatchingPosts = matchingPostService.findAllMatchingPost();
        List<MatchingPostData> matchingPosts = findMatchingPosts.stream()
                .map(matchingPost -> new MatchingPostData(
                        matchingPost.getTitle(),
                        matchingPost.getGender(),
                        matchingPost.getDesiredNumPeople(),
                        "seolhuigwan", // 임시 작성자
                        matchingPost.getCreatedAt()
                ))
                .collect(Collectors.toList());
        return new FindMatchingPostResponse<MatchingPostData>(matchingPosts.size(), matchingPosts);
    }

    @PostMapping("/api/v1/matchingposts")
    public CreateMatchingPostResponse postMatchingPost(@RequestBody CreateMatchingPostRequest dto){
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
        Long newMatchingPost = matchingPostService.saveMatchingPost(matchingPost);
        return new CreateMatchingPostResponse(newMatchingPost);
    }
}
