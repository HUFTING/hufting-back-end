package com.likelion.hufsting.domain.matching.dto.matchingpost;

import com.likelion.hufsting.domain.matching.domain.Gender;
import com.likelion.hufsting.domain.matching.domain.MatchingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class FindMatchingPostsData {
    private Long id;
    private String title;
    private Gender gender;
    private int desiredNumPeople;
    private MatchingStatus matchingStatus;
    private String authorName;
    private LocalDateTime createdAt;
}
