package com.likelion.hufsting.domain.matching.dto.matchingrequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;

@Data
public class UpdateMatchingReqRequest {
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;
    private Long matchingPostId;
    private List<Long> participantIds;
}
