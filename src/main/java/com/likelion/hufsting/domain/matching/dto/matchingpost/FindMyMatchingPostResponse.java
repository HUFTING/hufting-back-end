package com.likelion.hufsting.domain.matching.dto.matchingpost;

import com.likelion.hufsting.global.dto.ResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FindMyMatchingPostResponse implements ResponseDto {
    List<FindMyMatchingPostData> data;
}
