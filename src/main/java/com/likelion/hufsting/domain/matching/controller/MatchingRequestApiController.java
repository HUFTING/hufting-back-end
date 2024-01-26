package com.likelion.hufsting.domain.matching.controller;

import com.likelion.hufsting.domain.matching.dto.matchingrequest.*;
import com.likelion.hufsting.domain.matching.repository.query.MatchingRequestQueryRepository;
import com.likelion.hufsting.domain.matching.service.MatchingRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MatchingRequestApiController {
    private final MatchingRequestService matchingRequestService;

    // 내 매칭 신청 조회
    @GetMapping("/api/v1/my-matchingrequests")
    public ResponseEntity<FindMyMatchingReqResponse> getMyMatchingRequests(){
        try {
            log.info("Request to get my matchingrequests");
            FindMyMatchingReqResponse response = matchingRequestService.findMyMatchingRequest();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
    }

    // 매칭 신청
    @PostMapping("/api/v1/matchingrequests")
    public ResponseEntity<CreateMatchingReqResponse> postMatchingRequest(@RequestBody CreateMatchingReqRequest dto){
        log.info("Request to post matching request");
        CreateMatchingReqData convertedDto = CreateMatchingReqData.toCreateMatchingReqData(dto);
        CreateMatchingReqResponse response = matchingRequestService.createMatchingRequests(convertedDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 매칭 취소
    @DeleteMapping("/api/v1/matchingrequests/{matchingrequestid}")
    public ResponseEntity<Long> deleteMatchingRequest(@PathVariable("matchingrequestid") Long matchingRequestId){
        log.info("Request to delete matching request {}", matchingRequestId);
        try{
            matchingRequestService.removeMatchingRequest(matchingRequestId);
            return new ResponseEntity<>(matchingRequestId, HttpStatus.NO_CONTENT);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 매칭 수정
    @PutMapping("/api/v1/matchingrequests/{matchingrequestid}")
    public ResponseEntity<UpdateMatchingReqResponse> putMatchingRequest(@PathVariable("matchingrequestid") Long matchingRequestId,
                                   @RequestBody UpdateMatchingReqRequest dto){
        log.info("Request to put matching request {}", matchingRequestId);
        try{
            UpdateMatchingReqResponse response = matchingRequestService.updateMatchingRequest(
                    matchingRequestId,
                    UpdateMatchingReqData.toUpdateMatchingReqData(dto)
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch(IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 매칭 수락
    @PatchMapping("/api/v1/matchingrequests/{matchingrequestid}/accept")
    public ResponseEntity<AcceptMatchingRequestResponse> acceptMatchingRequest(@PathVariable("matchingrequestid") Long matchingRequestId){
        try{
            log.info("Request to accept matching request {}", matchingRequestId);
            AcceptMatchingRequestResponse response = matchingRequestService.acceptMatchingRequest(matchingRequestId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    // 매칭 거부
    @PatchMapping("/api/v1/matchingreqeusts/{matchingrequestid}/reject")
    public ResponseEntity<RejectMatchingRequestResponse> rejectMatchingRequest(@PathVariable("matchingrequestid") Long matchingRequestId){
        try{
            log.info("Request to reject matching request {}", matchingRequestId);
            RejectMatchingRequestResponse response = matchingRequestService.rejectMatchingRequest(matchingRequestId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
