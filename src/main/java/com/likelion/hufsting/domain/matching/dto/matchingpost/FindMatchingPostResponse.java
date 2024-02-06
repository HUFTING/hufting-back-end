package com.likelion.hufsting.domain.matching.dto.matchingpost;

import com.likelion.hufsting.global.domain.Gender;
import com.likelion.hufsting.domain.matching.domain.MatchingStatus;
import com.likelion.hufsting.global.dto.ResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FindMatchingPostResponse implements ResponseDto {
    private String title;
    private Gender gender;
    private int desiredNumPeople;
    private MatchingStatus matchingStatus;
    private List<FindMatchingPostParticipantData> participants;
}
