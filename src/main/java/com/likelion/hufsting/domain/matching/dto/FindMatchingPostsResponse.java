package com.likelion.hufsting.domain.matching.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FindMatchingPostsResponse<T> {
    private int count;
    private List<T> data;
}
