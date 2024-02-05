package com.likelion.hufsting.domain.matching.controller;

import com.likelion.hufsting.domain.matching.domain.MatchingPost;
import com.likelion.hufsting.domain.matching.dto.matchingpost.*;
import com.likelion.hufsting.domain.matching.exception.MatchingPostException;
import com.likelion.hufsting.domain.matching.service.MatchingPostService;
import com.likelion.hufsting.domain.matching.validation.PathIdFormat;
import com.likelion.hufsting.domain.profile.exception.ProfileException;
import com.likelion.hufsting.global.dto.ErrorResponse;
import com.likelion.hufsting.global.dto.ResponseDto;
import com.likelion.hufsting.global.exception.AuthException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
public class MatchingPostApiController {
    private final String PROFILE_ERR_MSG_KEY = "profile";
    private final String MATCHING_POST_ERR_MSG_KEY = "matchingPost";
    private final String AUTHENTICATION_ERR_MSG_KEY = "authentication";
    private final String NOT_FOUND_ERR_MSG = "notFound";
    // service
    private final MatchingPostService matchingPostService;

    @GetMapping("/api/v1/matchingposts")
    public FindMatchingPostsResponse<FindMatchingPostsData> getMatchingPosts(){
        log.info("Request to get matching posts");
        List<MatchingPost> findMatchingPosts = matchingPostService.findAllMatchingPost();
        List<FindMatchingPostsData> matchingPosts = findMatchingPosts.stream()
                .map(matchingPost -> new FindMatchingPostsData(
                        matchingPost.getId(),
                        matchingPost.getTitle(),
                        matchingPost.getGender(),
                        matchingPost.getDesiredNumPeople(),
                        matchingPost.getMatchingStatus(),
                        matchingPost.getAuthor().getName(),
                        matchingPost.getCreatedAt()
                ))
                .collect(Collectors.toList());
        return new FindMatchingPostsResponse<FindMatchingPostsData>(matchingPosts.size(), matchingPosts);
    }

    @GetMapping("/api/v1/my-matchingposts")
    public ResponseEntity<ResponseDto> getMyMatchingPost(Authentication authentication){
        try {
            log.info("Request to get my matching posts");
            FindMyMatchingPostResponse response = matchingPostService.findMyMatchingPost(authentication);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
    }

    @PostMapping("/api/v1/matchingposts")
    public ResponseEntity<ResponseDto> postMatchingPost(@RequestBody @Valid CreateMatchingPostRequest dto, Authentication authentication){
        try {
            log.info("Request to post matching post");
            // Converting DTO
            CreateMatchingPostData convertedDto = CreateMatchingPostData.toCreateMatchingPostData(dto);
            Long newMatchingPostId = matchingPostService.saveMatchingPost(convertedDto, authentication);
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
                    findMatchingPost.getAuthor().getName(), // 임시 사용자
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
                                                           @PathIdFormat Long matchingPostId, @RequestBody @Valid UpdateMatchingPostRequest dto,
                                                       Authentication authentication){
        log.info("Request to update matching post: {}", matchingPostId);
        try {
            Long updateMatchingPostId = matchingPostService.updateMatchingPost(
                    matchingPostId,
                    authentication,
                    UpdateMatchingPostData.toUpdateMatchingPostData(dto)
            );
            UpdateMatchingPostResponse response = new UpdateMatchingPostResponse(updateMatchingPostId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (IllegalArgumentException e){
            ErrorResponse response = ErrorResponse.createSingleResponseErrorMessage(
                    NOT_FOUND_ERR_MSG,
                    e.getMessage()
            );
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
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
        }catch (AuthException e){
            log.error(e.getMessage());
            ErrorResponse response = ErrorResponse.createSingleResponseErrorMessage(
                    AUTHENTICATION_ERR_MSG_KEY,
                    e.getMessage()
            );
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/api/v1/matchingposts/{matchingpostid}")
    public ResponseEntity<ResponseDto> deleteMatchingPost(@PathVariable("matchingpostid")
                                                       @PathIdFormat Long matchingPostId,
                                                          Authentication authentication){
        log.info("Request to delete matching post: {}", matchingPostId);
        try {
            matchingPostService.removeMatchingPost(matchingPostId, authentication);
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
