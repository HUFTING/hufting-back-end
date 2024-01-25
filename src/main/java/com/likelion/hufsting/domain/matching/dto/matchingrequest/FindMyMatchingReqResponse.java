package com.likelion.hufsting.domain.matching.dto.matchingrequest;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FindMyMatchingReqResponse {
    private List<FindMyMatchingReqData> data; // 요청 현황
}
