package com.likelion.hufsting.domain.matching.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class CreateMatchingReqResponse {
    private final List<Long> ids;
}
