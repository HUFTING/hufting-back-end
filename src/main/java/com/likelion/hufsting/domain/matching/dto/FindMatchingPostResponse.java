package com.likelion.hufsting.domain.matching.dto;

import com.likelion.hufsting.domain.matching.domain.Gender;
import com.likelion.hufsting.domain.matching.domain.MatchingPost;
import com.likelion.hufsting.domain.matching.domain.MatchingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindMatchingPostResponse {
    private String title;
    private String content;
    private Gender gender;
    private int desiredNumPeople;
    private String authorName;
    private String openTalkLink;
    private MatchingStatus matchingStatus;
}
