package com.likelion.hufsting.domain.matching.dto.matchingpost;

import com.likelion.hufsting.domain.matching.domain.MatchingStatus;
import com.likelion.hufsting.domain.matching.dto.matchingrequest.FindMatchingReqInPostData;
import com.likelion.hufsting.global.domain.Gender;
import com.likelion.hufsting.global.dto.ResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FindMyMatchingPostResponse implements ResponseDto {
    // 매칭글 관련
    private Long id;
    private String title;
    private int desiredNumPeople;
    private Gender gender;
    private String openTalkLink;
    private MatchingStatus matchingStatus;
    private List<FindMyMatchingPostInHostData> matchingHosts;
    // 매칭 요청 관련
    private int matchingRequestsCount;
    private List<FindMatchingReqInPostData> matchingRequests;
    // 글 작성자 이메일
    private String representativeEmail;
}
