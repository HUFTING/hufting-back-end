package com.likelion.hufsting.domain.matching.controller;

import com.likelion.hufsting.domain.matching.domain.MatchingPost;
import com.likelion.hufsting.domain.matching.dto.matchingpost.*;
import com.likelion.hufsting.domain.matching.exception.MatchingPostException;
import com.likelion.hufsting.domain.matching.service.MatchingPostService;
import com.likelion.hufsting.domain.matching.validation.PathIdFormat;
import com.likelion.hufsting.domain.oauth.domain.Member;
import com.likelion.hufsting.domain.profile.exception.ProfileException;
import com.likelion.hufsting.global.dto.ErrorResponse;
import com.likelion.hufsting.global.dto.ResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
public class MatchingPostApiController {
    private final String PROFILE_ERR_MSG_KEY = "profile";
    private final String MATCHING_POST_ERR_MSG_KEY = "matchingPost";
    // service
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
    public ResponseEntity<ResponseDto> getMyMatchingPost(){
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
    public ResponseEntity<ResponseDto> postMatchingPost(@RequestBody @Valid CreateMatchingPostRequest dto){
        try {
            log.info("Request to post matching post");
            // Converting DTO
            System.out.println(dto.getParticipants());
            CreateMatchingPostData convertedDto = CreateMatchingPostData.toCreateMatchingPostData(dto);
            Long newMatchingPostId = matchingPostService.saveMatchingPost(convertedDto);
            CreateMatchingPostResponse response = new CreateMatchingPostResponse(newMatchingPostId);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }catch (IllegalArgumentException e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (MatchingPostException e){
            log.error(e.getMessage());
            ErrorResponse response = ErrorResponse.createSingleResponseErrorMessage(
                    MATCHING_POST_ERR_MSG_KEY,
                    e.getMessage()
            );
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }catch (ProfileException e){
            log.error(e.getMessage());
            ErrorResponse response = ErrorResponse.createSingleResponseErrorMessage(
                    PROFILE_ERR_MSG_KEY,
                    e.getMessage()
            );
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/api/v1/matchingposts/{matchingpostid}")
    public ResponseEntity<ResponseDto> getMatchingPost(@PathVariable("matchingpostid")
                                                                        @PathIdFormat Long matchingPostId){
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
    public ResponseEntity<ResponseDto> putMatchingPost(@PathVariable("matchingpostid")
                                                                          @PathIdFormat Long matchingPostId,
                                           @RequestBody @Valid UpdateMatchingPostRequest dto){
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
        }catch (MatchingPostException e){
            log.error(e.getMessage());
            ErrorResponse response = ErrorResponse.createSingleResponseErrorMessage(
                    MATCHING_POST_ERR_MSG_KEY,
                    e.getMessage()
            );
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }catch (ProfileException e){
            log.error(e.getMessage());
            ErrorResponse response = ErrorResponse.createSingleResponseErrorMessage(
                    PROFILE_ERR_MSG_KEY,
                    e.getMessage()
            );
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/api/v1/matchingposts/{matchingpostid}")
    public ResponseEntity<ResponseDto> deleteMatchingPost(@PathVariable("matchingpostid")
                                                       @PathIdFormat Long matchingPostId){
        log.info("Request to delete matching post: {}", matchingPostId);
        try {
            matchingPostService.removeMatchingPost(matchingPostId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (MatchingPostException e){
            log.error(e.getMessage());
            ErrorResponse response = ErrorResponse.createSingleResponseErrorMessage(
                    MATCHING_POST_ERR_MSG_KEY,
                    e.getMessage()
            );
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
