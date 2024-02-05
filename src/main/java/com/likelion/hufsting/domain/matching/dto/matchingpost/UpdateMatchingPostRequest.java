package com.likelion.hufsting.domain.matching.dto.matchingpost;

import com.likelion.hufsting.global.domain.Gender;
import com.likelion.hufsting.global.validation.EnumFormat;
import com.likelion.hufsting.domain.matching.validation.NumOfPeopleEqual;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@NumOfPeopleEqual(numField = "desiredNumPeople", listField = "participants")
public class UpdateMatchingPostRequest {
    // 제목
    @NotBlank(message = "제목이 입력되지 않았습니다.")
    private String title;
    // 성별
    @EnumFormat(enumClass = Gender.class)
    private Gender gender;
    // 희망 인원 수
    @NotNull(message = "희망 인원수가 입력되지 않았습니다.")
    @Min(value = 1, message = "희망 인원수는 최소 1명 이상 입니다.")
    @Max(value = 4, message = "희망 인원수는 최대 4명 이하 입니다.")
    private int desiredNumPeople;
    // 오픈 채팅 링크
    @NotBlank(message = "오픈 카톡 링크가 입력되지 않았습니다.")
    @Pattern(regexp = "^(https:\\/\\/open\\.kakao\\.com/).*$", message = "올바른 오픈 카톡 링크를 입력해주세요.")
    private String openTalkLink;
    // 참여자 ID
    private List<Long> participants;
}
