package com.likelion.hufsting.domain.matching.dto.matchingpost;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class SearchingMatchingPostResponse {
    private String title;
    private int desiredNumPeople;
    private String author;
}
