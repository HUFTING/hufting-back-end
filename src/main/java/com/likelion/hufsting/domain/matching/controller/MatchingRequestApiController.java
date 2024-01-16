package com.likelion.hufsting.domain.matching.controller;

import com.likelion.hufsting.domain.matching.dto.CreateMatchingReqData;
import com.likelion.hufsting.domain.matching.dto.CreateMatchingReqRequest;
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

    // 매칭 신청
    @PostMapping("/api/v1/matchingrequests")
    public ResponseEntity<Void> postMatchingRequest(@RequestBody CreateMatchingReqRequest dto){
        log.info("Request to post matching request");
        CreateMatchingReqData convertedDto = CreateMatchingReqData.toCreateMatchingReqData(dto);
        matchingRequestService.createMatchingRequests(convertedDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // 매칭 취소
    @DeleteMapping("/api/v1/matchingrequests/{matchingrequestid}")
    public ResponseEntity<Long> deleteMatchingRequest(@PathVariable("matchingrequestid") Long matchingRequestId){
        try{
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
