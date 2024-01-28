package com.likelion.hufsting.domain.matching.dto.matchingrequest;

import com.likelion.hufsting.global.dto.ResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class UpdateMatchingReqResponse implements ResponseDto {
    private final Long matchingRequestId;
    private final List<Long> participantIds;
}
