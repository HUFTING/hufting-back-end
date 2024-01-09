package com.likelion.hufsting.domain.matching.controller;

import com.likelion.hufsting.domain.matching.domain.MatchingPost;
import com.likelion.hufsting.domain.matching.domain.MatchingStatus;
import com.likelion.hufsting.domain.matching.dto.*;
import com.likelion.hufsting.domain.matching.service.MatchingPostService;
import com.likelion.hufsting.domain.profile.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MatchingPostApiController {
    private final MatchingPostService matchingPostService;

    @GetMapping("/api/v1/matchingposts")
    public FindMatchingPostsResponse<MatchingPostsData> getMatchingPosts(){
        List<MatchingPost> findMatchingPosts = matchingPostService.findAllMatchingPost();
        List<MatchingPostsData> matchingPosts = findMatchingPosts.stream()
                .map(matchingPost -> new MatchingPostsData(
                        matchingPost.getTitle(),
                        matchingPost.getGender(),
                        matchingPost.getDesiredNumPeople(),
                        "설**", // 임시 작성자
                        matchingPost.getCreatedAt()
                ))
                .collect(Collectors.toList());
        return new FindMatchingPostsResponse<MatchingPostsData>(matchingPosts.size(), matchingPosts);
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

    @GetMapping("/api/v1/matchingposts/{matchingpostid}")
    public FindMatchingPostResponse getMatchingPost(@PathVariable("matchingpostid") Long matchingPostId){
        MatchingPost findMatchingPost = matchingPostService.findByIdMatchingPost(matchingPostId);
        return new FindMatchingPostResponse(
                findMatchingPost.getTitle(),
                findMatchingPost.getContent(),
                findMatchingPost.getGender(),
                findMatchingPost.getDesiredNumPeople(),
                "원**", // 임시 사용자
                findMatchingPost.getOpenTalkLink(),
                findMatchingPost.getMatchingStatus()
        );
    }
}
