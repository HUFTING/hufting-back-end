package com.likelion.hufsting.domain.matching.dto.matchingrequest;

import lombok.Data;

import java.util.List;

@Data
public class CreateMatchingReqRequest {
    private Long matchingPostId; // 매칭글 ID
    private List<Long> participantIds; // 참가자들 ID
}
