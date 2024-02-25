package com.likelion.hufsting.domain.matching.dto.matchingpost;

import com.likelion.hufsting.domain.matching.domain.MatchingHost;
import com.likelion.hufsting.global.dto.ResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class UpdateMatchingPostResponse implements ResponseDto {
    private final Long matchingPostId;
    private final List<UpdateMatchingPostHostData> matchingHosts;
}
