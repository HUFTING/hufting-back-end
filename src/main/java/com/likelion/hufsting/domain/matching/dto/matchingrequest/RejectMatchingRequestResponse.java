package com.likelion.hufsting.domain.matching.dto.matchingrequest;

import com.likelion.hufsting.global.dto.ResponseDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RejectMatchingRequestResponse implements ResponseDto {
    private Long matchingRequestId;
}
