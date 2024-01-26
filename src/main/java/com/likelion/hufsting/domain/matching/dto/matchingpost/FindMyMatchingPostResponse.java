package com.likelion.hufsting.domain.matching.dto.matchingpost;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FindMyMatchingPostResponse {
    List<FindMyMatchingPostData> data;
}
