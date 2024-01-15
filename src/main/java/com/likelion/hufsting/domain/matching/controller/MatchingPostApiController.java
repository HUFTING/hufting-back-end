package com.likelion.hufsting.domain.matching.controller;

import com.likelion.hufsting.domain.matching.domain.MatchingPost;
import com.likelion.hufsting.domain.matching.domain.MatchingStatus;
import com.likelion.hufsting.domain.matching.dto.*;
import com.likelion.hufsting.domain.matching.service.MatchingPostService;
import com.likelion.hufsting.domain.profile.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MatchingPostApiController {
    private final MatchingPostService matchingPostService;

    @GetMapping("/api/v1/matchingposts")
    public FindMatchingPostsResponse<FindMatchingPostsData> getMatchingPosts(){
        List<MatchingPost> findMatchingPosts = matchingPostService.findAllMatchingPost();
        List<FindMatchingPostsData> matchingPosts = findMatchingPosts.stream()
                .map(matchingPost -> new FindMatchingPostsData(
                        matchingPost.getTitle(),
                        matchingPost.getGender(),
                        matchingPost.getDesiredNumPeople(),
                        "설**", // 임시 작성자
                        matchingPost.getCreatedAt()
                ))
                .collect(Collectors.toList());
        return new FindMatchingPostsResponse<FindMatchingPostsData>(matchingPosts.size(), matchingPosts);
    }

    @PostMapping("/api/v1/matchingposts")
    public ResponseEntity<CreateMatchingPostResponse> postMatchingPost(@RequestBody CreateMatchingPostRequest dto){
        // Converting DTO
        System.out.println(dto.getParticipants());
        CreateMatchingPostData convertedDto = CreateMatchingPostData.toCreateMatchingPostData(dto);
        Long newMatchingPostId = matchingPostService.saveMatchingPost(convertedDto);
        CreateMatchingPostResponse response = new CreateMatchingPostResponse(newMatchingPostId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
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

    @PatchMapping("/api/v1/matchingposts/{matchingpostid}")
    public Long patchMatchingPost(@PathVariable("matchingpostid") Long matchingPostId,
                                           @RequestBody UpdateMatchingPostRequest dto){
        log.info("Request to update matching post: {}", matchingPostId);
        return matchingPostService.updateMatchingPost(
                matchingPostId,
                UpdateMatchingPostData.toUpdateMatchingPostData(dto)
        );
    }

    @DeleteMapping("/api/v1/matchingposts/{matchingpostid}")
    public ResponseEntity<Void> deleteMatchingPost(@PathVariable("matchingpostid") Long matchingPostId){
        log.info("Request to delete matching post: {}", matchingPostId);
        matchingPostService.removeMatchingPost(matchingPostId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
