package com.likelion.hufsting.domain.matching.controller;

import com.likelion.hufsting.domain.matching.domain.MatchingPost;
import com.likelion.hufsting.domain.matching.dto.matchingpost.*;
import com.likelion.hufsting.domain.matching.service.MatchingPostService;
import com.likelion.hufsting.domain.oauth.domain.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MatchingPostApiController {
    private final MatchingPostService matchingPostService;

    @GetMapping("/api/v1/matchingposts")
    public FindMatchingPostsResponse<FindMatchingPostsData> getMatchingPosts(){
        log.info("Request to get matching posts");
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

    @GetMapping("/api/v1/my-matchingposts")
    public ResponseEntity<FindMyMatchingPostResponse> getMyMatchingPost(){
        try {
            log.info("Request to get my matching posts");
            Member author = new Member(); // 임시 인증 유저
            FindMyMatchingPostResponse response = matchingPostService.findMyMatchingPost(author);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
    }

    @PostMapping("/api/v1/matchingposts")
    public ResponseEntity<CreateMatchingPostResponse> postMatchingPost(@RequestBody @Valid CreateMatchingPostRequest dto){
        log.info("Request to post matching post");
        // Converting DTO
        System.out.println(dto.getParticipants());
        CreateMatchingPostData convertedDto = CreateMatchingPostData.toCreateMatchingPostData(dto);
        Long newMatchingPostId = matchingPostService.saveMatchingPost(convertedDto);
        CreateMatchingPostResponse response = new CreateMatchingPostResponse(newMatchingPostId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/api/v1/matchingposts/{matchingpostid}")
    public ResponseEntity<FindMatchingPostResponse> getMatchingPost(@PathVariable("matchingpostid") Long matchingPostId){
        log.info("Request to get matching post: {}", matchingPostId);
        try {
            MatchingPost findMatchingPost = matchingPostService.findByIdMatchingPost(matchingPostId);
            FindMatchingPostResponse response = new FindMatchingPostResponse(
                    findMatchingPost.getTitle(),
                    findMatchingPost.getGender(),
                    findMatchingPost.getDesiredNumPeople(),
                    "원**", // 임시 사용자
                    findMatchingPost.getOpenTalkLink(),
                    findMatchingPost.getMatchingStatus()
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/api/v1/matchingposts/{matchingpostid}")
    public ResponseEntity<UpdateMatchingPostResponse> putMatchingPost(@PathVariable("matchingpostid") Long matchingPostId,
                                           @RequestBody UpdateMatchingPostRequest dto){
        log.info("Request to update matching post: {}", matchingPostId);
        try {
            Long updateMatchingPostId = matchingPostService.updateMatchingPost(
                    matchingPostId,
                    UpdateMatchingPostData.toUpdateMatchingPostData(dto)
            );
            UpdateMatchingPostResponse response = new UpdateMatchingPostResponse(updateMatchingPostId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/api/v1/matchingposts/{matchingpostid}")
    public ResponseEntity<Void> deleteMatchingPost(@PathVariable("matchingpostid") Long matchingPostId){
        log.info("Request to delete matching post: {}", matchingPostId);
        try {
            matchingPostService.removeMatchingPost(matchingPostId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
