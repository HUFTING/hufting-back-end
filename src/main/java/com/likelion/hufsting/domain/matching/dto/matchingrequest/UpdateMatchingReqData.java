package com.likelion.hufsting.domain.matching.dto.matchingrequest;

import lombok.Data;

import java.util.List;

@Data
public class UpdateMatchingReqData {
    private List<Long> participantIds;

    public static UpdateMatchingReqData toUpdateMatchingReqData(UpdateMatchingReqRequest dto){
        UpdateMatchingReqData updateMatchingReqData = new UpdateMatchingReqData();
        updateMatchingReqData.setParticipantIds(dto.getParticipantIds());
        return updateMatchingReqData;
    }
}

