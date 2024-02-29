package com.likelion.hufsting.domain.matching.dto.matchingrequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;

@Data
public class CreateMatchingReqRequest {
    @NotBlank(message = "제목을 입력해주세요.")
    private String title; // 제목
    private Long matchingPostId; // 매칭글 ID
    private List<Long> participantIds; // 참가자들 ID
}
