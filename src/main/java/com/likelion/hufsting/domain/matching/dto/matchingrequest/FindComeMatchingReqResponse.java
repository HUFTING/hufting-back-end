package com.likelion.hufsting.domain.matching.dto.matchingrequest;

import com.likelion.hufsting.domain.matching.domain.MatchingStatus;
import com.likelion.hufsting.global.dto.ResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FindComeMatchingReqResponse implements ResponseDto {
    private Long matchingRequestId;
    private String matchingRequestTitle;
    private MatchingStatus matchingStatus;
    private List<FindComeMatchingReqInParticipantData> participants;
    private List<FindComeMatchingReqInHostData> hosts;
}
