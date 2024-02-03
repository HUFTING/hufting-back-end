package com.likelion.hufsting.domain.matching.dto.matchingrequest;

import com.likelion.hufsting.domain.matching.domain.MatchingAcceptance;
import com.likelion.hufsting.domain.matching.domain.MatchingParticipant;
import com.likelion.hufsting.domain.matching.domain.MatchingRequest;
import lombok.Data;

import java.util.List;

@Data
public class FindMyMatchingReqData {
    // 매칭글 관련
    private Long matchingPostId; // 매칭글 ID
    private String matchingPostTitle; // 매칭글 제목
    // 매칭 신청 관련
    private MatchingAcceptance matchingAcceptance; // 매칭 신청 상태 : 수락/거부
    // 매칭 신청자 관련
    private Long representativeId; // 대표자 ID
    private String representativeName; // 대표자 이름

    // convert to FindMatchingReqData
    public static FindMyMatchingReqData toFindMatchingReqData(MatchingRequest dto){
        FindMyMatchingReqData findMatchingReqData = new FindMyMatchingReqData();
        findMatchingReqData.setMatchingPostId(dto.getMatchingPost().getId());
        findMatchingReqData.setMatchingPostTitle(dto.getMatchingPost().getTitle());
        findMatchingReqData.setMatchingAcceptance(dto.getMatchingAcceptance());
        findMatchingReqData.setRepresentativeId(dto.getRepresentative().getId());
        findMatchingReqData.setRepresentativeName(dto.getRepresentative().getName());
        return findMatchingReqData;
    }
}
