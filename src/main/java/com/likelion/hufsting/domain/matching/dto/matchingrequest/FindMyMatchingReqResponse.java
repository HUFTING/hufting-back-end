package com.likelion.hufsting.domain.matching.dto.matchingrequest;

import com.likelion.hufsting.global.dto.ResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FindMyMatchingReqResponse implements ResponseDto {
    private List<FindMyMatchingReqData> data; // 요청 현황
}
