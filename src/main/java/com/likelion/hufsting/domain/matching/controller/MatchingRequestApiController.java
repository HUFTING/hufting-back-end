package com.likelion.hufsting.domain.matching.controller;

import com.likelion.hufsting.domain.matching.dto.matchingrequest.*;
import com.likelion.hufsting.domain.matching.exception.MatchingReqException;
import com.likelion.hufsting.domain.matching.exception.MatchingPostException;
import com.likelion.hufsting.domain.matching.service.MatchingRequestService;
import com.likelion.hufsting.domain.matching.validation.PathIdFormat;
import com.likelion.hufsting.global.dto.ResponseDto;
import com.likelion.hufsting.global.dto.ErrorResponse;
import com.likelion.hufsting.global.exception.AuthenticationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class MatchingRequestApiController {
    // constant
    private final String MATCHING_REQ_ERR_MSG_KEY = "matchingRequest";
    private final String MATCHING_POST_ERR_MSG_KEY = "matchingPost";
    private final String MATCHING_REQ_AUTHENTICATION_ERR_MSG_KEY = "authentication";
    // service
    private final MatchingRequestService matchingRequestService;
    // 내 매칭 신청 조회
    @GetMapping("/api/v1/my-matchingrequests")
    public ResponseEntity<ResponseDto> getMyMatchingRequests(Authentication authentication){
        try {
            log.info("Request to get my matchingrequests");
            FindMyMatchingReqResponse response = matchingRequestService.findMyMatchingRequest(authentication);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
    }

    // 매칭 신청
    @PostMapping("/api/v1/matchingrequests")
    public ResponseEntity<ResponseDto> postMatchingRequest(@RequestBody @Valid CreateMatchingReqRequest dto, Authentication authentication){
        try {
            log.info("Request to post matching request");
            CreateMatchingReqData convertedDto = CreateMatchingReqData.toCreateMatchingReqData(dto);
            CreateMatchingReqResponse response = matchingRequestService.createMatchingRequests(convertedDto, authentication);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }catch (IllegalArgumentException e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (MatchingReqException e){
            log.error(e.getMessage());
            ErrorResponse response = ErrorResponse.createSingleResponseErrorMessage(
                    MATCHING_REQ_ERR_MSG_KEY,
                    e.getMessage()
            );
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }catch (MatchingPostException e){
            log.error(e.getMessage());
            ErrorResponse response = ErrorResponse.createSingleResponseErrorMessage(
                    MATCHING_POST_ERR_MSG_KEY,
                    e.getMessage()
            );
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }catch (AuthenticationException e){
            log.error(e.getMessage());
            ErrorResponse response = ErrorResponse.createSingleResponseErrorMessage(
                    MATCHING_REQ_AUTHENTICATION_ERR_MSG_KEY,
                    e.getMessage()
            );
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // 매칭 취소
    @DeleteMapping("/api/v1/matchingrequests/{matchingrequestid}")
    public ResponseEntity<ResponseDto> deleteMatchingRequest(
            @PathVariable("matchingrequestid") @PathIdFormat Long matchingRequestId,
            Authentication authentication){
        log.info("Request to delete matching request {}", matchingRequestId);
        try{
            matchingRequestService.removeMatchingRequest(matchingRequestId, authentication);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
        }catch (AuthenticationException e){
            log.error(e.getMessage());
            ErrorResponse response = ErrorResponse.createSingleResponseErrorMessage(
                    MATCHING_REQ_AUTHENTICATION_ERR_MSG_KEY,
                    e.getMessage()
            );
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // 매칭 수정
    @PutMapping("/api/v1/matchingrequests/{matchingrequestid}")
    public ResponseEntity<ResponseDto> putMatchingRequest(
            @PathVariable("matchingrequestid") @PathIdFormat Long matchingRequestId,
            @RequestBody @Valid UpdateMatchingReqRequest dto, Authentication authentication){
        try{
            log.info("Request to put matching request {}", matchingRequestId);
            UpdateMatchingReqResponse response = matchingRequestService.updateMatchingRequest(
                    matchingRequestId,
                    UpdateMatchingReqData.toUpdateMatchingReqData(dto),
                    authentication
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch(IllegalArgumentException e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (MatchingReqException e){
            log.error(e.getMessage());
            ErrorResponse response = ErrorResponse.createSingleResponseErrorMessage(
                    MATCHING_REQ_ERR_MSG_KEY,
                    e.getMessage()
            );
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }catch (AuthenticationException e){
            log.error(e.getMessage());
            ErrorResponse response = ErrorResponse.createSingleResponseErrorMessage(
                    MATCHING_REQ_AUTHENTICATION_ERR_MSG_KEY,
                    e.getMessage()
            );
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // 매칭 수락
    @PatchMapping("/api/v1/matchingrequests/{matchingrequestid}/accept")
    public ResponseEntity<ResponseDto> acceptMatchingRequest(
            @PathVariable("matchingrequestid") @PathIdFormat Long matchingRequestId, Authentication authentication){
        try{
            log.info("Request to accept matching request {}", matchingRequestId);
            AcceptMatchingRequestResponse response = matchingRequestService.acceptMatchingRequest(matchingRequestId, authentication);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (IllegalArgumentException e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (MatchingReqException e){
            log.error(e.getMessage());
            ErrorResponse response = ErrorResponse.createSingleResponseErrorMessage(
                    MATCHING_REQ_ERR_MSG_KEY,
                    e.getMessage()
            );
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }catch (MatchingPostException e){
            log.error(e.getMessage());
            ErrorResponse response = ErrorResponse.createSingleResponseErrorMessage(
                    MATCHING_POST_ERR_MSG_KEY,
                    e.getMessage()
            );
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    // 매칭 거부
    @PatchMapping("/api/v1/matchingrequests/{matchingrequestid}/reject")
    public ResponseEntity<ResponseDto> rejectMatchingRequest(
            @PathVariable("matchingrequestid") @PathIdFormat Long matchingRequestId, Authentication authentication){
        try{
            log.info("Request to reject matching request {}", matchingRequestId);
            RejectMatchingRequestResponse response = matchingRequestService.rejectMatchingRequest(matchingRequestId, authentication);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (IllegalArgumentException e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (MatchingReqException e){
            log.error(e.getMessage());
            ErrorResponse response = ErrorResponse.createSingleResponseErrorMessage(
                    MATCHING_REQ_ERR_MSG_KEY,
                    e.getMessage()
            );
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
