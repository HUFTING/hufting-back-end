package com.likelion.hufsting.domain.matching.dto.matchingrequest;

import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;

@Data
public class UpdateMatchingReqRequest {
    @Positive
    private Long matchingPostId;
    private List<Long> participantIds;
}
