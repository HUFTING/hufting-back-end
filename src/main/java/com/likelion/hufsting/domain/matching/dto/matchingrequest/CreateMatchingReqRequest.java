package com.likelion.hufsting.domain.matching.dto.matchingrequest;

import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;

@Data
public class CreateMatchingReqRequest {
    @Positive
    private Long matchingPostId; // 매칭글 ID
    private List<Long> participantIds; // 참가자들 ID
}
