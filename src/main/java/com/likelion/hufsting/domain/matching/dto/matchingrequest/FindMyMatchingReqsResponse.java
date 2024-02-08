package com.likelion.hufsting.domain.matching.dto.matchingrequest;

import com.likelion.hufsting.global.dto.ResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FindMyMatchingReqsResponse implements ResponseDto {
    private List<FindMyMatchingReqsData> data; // 요청 현황
}
