package com.likelion.hufsting.domain.matching.dto.matchingrequest;

import com.likelion.hufsting.global.dto.ResponseDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AcceptMatchingRequestResponse implements ResponseDto {
    private Long matchingRequestId; // 매칭 요청 ID
    private Long matchingPostId; // 매칭 글 ID
    private Long alarmId; // 알람 ID
}
