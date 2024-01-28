package com.likelion.hufsting.domain.matching.dto.matchingpost;

import com.likelion.hufsting.domain.matching.domain.Gender;
import com.likelion.hufsting.domain.matching.domain.MatchingPost;
import com.likelion.hufsting.domain.matching.domain.MatchingStatus;
import com.likelion.hufsting.global.dto.ResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindMatchingPostResponse implements ResponseDto {
    private String title;
    private Gender gender;
    private int desiredNumPeople;
    private String authorName;
    private String openTalkLink;
    private MatchingStatus matchingStatus;
}
