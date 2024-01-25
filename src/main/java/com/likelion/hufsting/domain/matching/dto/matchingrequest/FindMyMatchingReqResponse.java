package com.likelion.hufsting.domain.matching.dto.matchingrequest;

import lombok.Data;

import java.util.List;

@Data
public class FindMyMatchingReqResponse {
    private List<FindMyMatchingReqData> data; // 요청 현황
}
