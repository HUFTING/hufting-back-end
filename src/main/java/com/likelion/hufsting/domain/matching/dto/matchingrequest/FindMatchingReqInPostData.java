package com.likelion.hufsting.domain.matching.dto.matchingrequest;

import com.likelion.hufsting.domain.matching.domain.MatchingAcceptance;
import com.likelion.hufsting.domain.matching.domain.MatchingRequest;
import lombok.Data;

@Data
public class FindMatchingReqInPostData {
    private Long matchingRequestId; // 매칭 요청 ID
    private String matchingRequestTitle; // 매칭 요청 제목

    // convert to FindMatchingReqInPostData
    public static FindMatchingReqInPostData toFindMatchingReqInPostData(MatchingRequest matchingRequest){
        FindMatchingReqInPostData matchingReqInPostData = new FindMatchingReqInPostData();
        matchingReqInPostData.setMatchingRequestId(matchingRequest.getId());
        matchingReqInPostData.setMatchingRequestTitle(matchingRequest.getTitle());
        return matchingReqInPostData;
    }
}
