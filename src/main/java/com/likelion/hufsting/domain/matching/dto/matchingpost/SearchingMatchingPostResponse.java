package com.likelion.hufsting.domain.matching.dto.matchingpost;


import com.likelion.hufsting.global.domain.Gender;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class SearchingMatchingPostResponse {
    private String title;
    private int desiredNumPeople;
    private Gender gender;
    private String author;
    private LocalDateTime createdAt;

}
