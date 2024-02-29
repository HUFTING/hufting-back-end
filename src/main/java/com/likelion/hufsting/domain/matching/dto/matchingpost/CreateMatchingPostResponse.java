package com.likelion.hufsting.domain.matching.dto.matchingpost;

import com.likelion.hufsting.global.dto.ResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateMatchingPostResponse implements ResponseDto {
    private final Long matchingPostId;
}
