package com.likelion.hufsting.domain.matching.dto;

import com.likelion.hufsting.domain.matching.domain.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UpdateMatchingPostRequest {
    // 제목
    private String title;
    // 내용
    private String content;
    // 성별
    private Gender gender;
    // 희망 인원 수
    private int desiredNumPeople;
    // 오픈 채팅 링크
    private String openTalkLink;
    // 참여자 ID
    private List<Integer> participants;
}