package com.likelion.hufsting.domain.matching.dto.matchingpost;

import com.likelion.hufsting.domain.profile.domain.Mbti;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class FindMatchingPostParticipantData {
    private String name;
    private String studentNumber;
    private LocalDate age;
    private Mbti mbti;
    private String content;
}
