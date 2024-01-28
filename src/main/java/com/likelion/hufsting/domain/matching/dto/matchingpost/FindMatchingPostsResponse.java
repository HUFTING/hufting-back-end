package com.likelion.hufsting.domain.matching.dto.matchingpost;

import com.likelion.hufsting.global.dto.ResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FindMatchingPostsResponse<T> implements ResponseDto {
    private int count;
    private List<T> data;
}
