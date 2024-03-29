package com.likelion.hufsting.domain.matching.dto.matchingrequest;

import lombok.Data;

import java.util.List;

@Data
public class UpdateMatchingReqData {
    private String title;
    private Long MatchingPostId;
    private List<Long> participantIds;

    public static UpdateMatchingReqData toUpdateMatchingReqData(UpdateMatchingReqRequest dto){
        UpdateMatchingReqData updateMatchingReqData = new UpdateMatchingReqData();
        updateMatchingReqData.setTitle(dto.getTitle());
        updateMatchingReqData.setMatchingPostId(dto.getMatchingPostId());
        updateMatchingReqData.setParticipantIds(dto.getParticipantIds());
        return updateMatchingReqData;
    }
}

