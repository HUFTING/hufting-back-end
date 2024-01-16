package com.likelion.hufsting.domain.matching.dto;

import lombok.Data;

import java.util.List;

@Data
public class UpdateMatchingReqData {
    private List<Long> ids;

    public static UpdateMatchingReqData toUpdateMatchingReqData(UpdateMatchingReqRequest dto){
        UpdateMatchingReqData updateMatchingReqData = new UpdateMatchingReqData();
        updateMatchingReqData.setIds(dto.getParticipantsIds());
        return updateMatchingReqData;
    }
}

