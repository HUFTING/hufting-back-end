package com.likelion.hufsting.domain.matching.dto.matchingpost;

import com.likelion.hufsting.domain.profile.domain.Mbti;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class FindMatchingPostParticipantData {
    private Long id;
    private String name;
    private String major;
    private String studentNumber;
    private String age;
    private Mbti mbti;
    private String content;
}
