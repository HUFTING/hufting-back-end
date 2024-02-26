package com.likelion.hufsting.domain.matching.controller;

import com.likelion.hufsting.domain.matching.dto.matchingpost.*;
import com.likelion.hufsting.domain.matching.exception.MatchingPostException;
import com.likelion.hufsting.domain.matching.service.MatchingPostService;
import com.likelion.hufsting.domain.matching.validation.PathIdFormat;
import com.likelion.hufsting.domain.profile.exception.ProfileException;
import com.likelion.hufsting.global.dto.ErrorResponse;
import com.likelion.hufsting.global.dto.ResponseDto;
import com.likelion.hufsting.global.exception.AuthException;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ResponseDto> getMatchingPosts(@PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC)
                                                        Pageable pageable){
        log.info("Request to get matching posts");
        FindMatchingPostsResponse<FindMatchingPostsData> response = matchingPostService.findAllMatchingPost(pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/api/v1/my-matchingposts")
    public ResponseEntity<ResponseDto> getMyMatchingPosts(Authentication authentication){
        try {
            log.info("Request to get my matching posts");
            FindMyMatchingPostsResponse response = matchingPostService.findMyMatchingPosts(authentication);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
    }

    @GetMapping("/api/v1/my-matchingposts/{matchingpostid}")
    public ResponseEntity<ResponseDto> getMyMatchingPost(@PathVariable("matchingpostid") Long matchingPostId, Authentication authentication){
        try {
            log.info("Request to get my matching post");
            FindMyMatchingPostResponse response = matchingPostService.findMyMatchingPost(matchingPostId, authentication);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (IllegalArgumentException e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (MatchingPostException e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
                                                                        @PathIdFormat Long matchingPostId, Authentication authentication){
        log.info("Request to get matching post: {}", matchingPostId);
        try {
            FindMatchingPostResponse response = matchingPostService.findByIdMatchingPost(matchingPostId);
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
            UpdateMatchingPostResponse response = matchingPostService.updateMatchingPost(
                    matchingPostId,
                    authentication,
                    UpdateMatchingPostData.toUpdateMatchingPostData(dto)
            );
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

    @GetMapping("/api/v1/searchingtitle")
    public ResponseEntity<List<SearchingMatchingPostResponse>> searchingMatchPost(@RequestParam("title") String title) {
        List<SearchingMatchingPostResponse> searchResultList = matchingPostService.findOneMatchingPost(title);
        return ResponseEntity.ok(searchResultList);
    }
}
