package com.likelion.hufsting.domain.matching.dto.matchingrequest;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class CreateMatchingReqResponse {
    private final Long matchingRequestId;
    private final List<Long> participantIds;
}