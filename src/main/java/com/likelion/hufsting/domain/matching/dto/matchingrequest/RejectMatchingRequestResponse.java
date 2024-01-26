package com.likelion.hufsting.domain.matching.dto.matchingrequest;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RejectMatchingRequestResponse {
    private Long matchingRequestId;
}
