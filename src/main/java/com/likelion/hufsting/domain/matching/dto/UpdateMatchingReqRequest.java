package com.likelion.hufsting.domain.matching.dto;

import lombok.Data;

import java.util.List;

@Data
public class UpdateMatchingReqRequest {
    private List<Long> participantIds;
}
