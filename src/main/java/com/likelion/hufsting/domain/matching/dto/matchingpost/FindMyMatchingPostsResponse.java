package com.likelion.hufsting.domain.matching.dto.matchingpost;

import com.likelion.hufsting.global.dto.ResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FindMyMatchingPostsResponse implements ResponseDto {
    private int count;
    private List<FindMyMatchingPostsData> data;
}
