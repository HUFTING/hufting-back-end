package com.likelion.hufsting.domain.matching.dto.matchingrequest;

import com.likelion.hufsting.domain.matching.domain.MatchingAcceptance;
import com.likelion.hufsting.domain.matching.domain.MatchingRequest;
import lombok.Data;

@Data
public class FindMatchingReqInPostData {
    private Long representativeId; // 대표 신청자 ID
    // private String representativeName; // 대표 신청자 이름
    private MatchingAcceptance matchingAcceptance; // 매칭 수락 현황

    // convert to FindMatchingReqInPostData
    public static FindMatchingReqInPostData toFindMatchingReqInPostData(MatchingRequest matchingRequest){
        FindMatchingReqInPostData matchingReqInPostData = new FindMatchingReqInPostData();
        matchingReqInPostData.setRepresentativeId(matchingRequest.getRepresentative().getId());
        /*
        matchingReqInPostData.setRepresentativeName(
                matchingRequest.
                getRepresentative().
                getProfile().
                getName());
        */
        matchingReqInPostData.setMatchingAcceptance(matchingRequest.getMatchingAcceptance());
        return matchingReqInPostData;
    }
}
