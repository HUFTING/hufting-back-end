package com.likelion.hufsting.domain.matching.dto.matchingrequest;

import lombok.Data;

import java.util.List;

@Data
public class CreateMatchingReqData {
    private String title;
    private Long matchingPostId; // 매칭글 ID
    private List<Long> participantIds; // 참가자들 ID

    // from CreateMatchingReqRequest to CreateMatchingReqData
    public static CreateMatchingReqData toCreateMatchingReqData(CreateMatchingReqRequest dto){
        CreateMatchingReqData createMatchingReqData = new CreateMatchingReqData();
        createMatchingReqData.setTitle(dto.getTitle());
        createMatchingReqData.setMatchingPostId(dto.getMatchingPostId());
        createMatchingReqData.setParticipantIds(dto.getParticipantIds());
        return createMatchingReqData;
    }
}
