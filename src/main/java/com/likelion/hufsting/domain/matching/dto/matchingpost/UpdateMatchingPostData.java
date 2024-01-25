package com.likelion.hufsting.domain.matching.dto.matchingpost;

import com.likelion.hufsting.domain.matching.domain.Gender;
import lombok.Data;

import java.util.List;

@Data
public class UpdateMatchingPostData {
    private String title;
    private String content;
    private Gender gender;
    private int desiredNumPeople;
    private String openTalkLink;
    private List<Long> participants;

    // Controller-Service 간 DTO 변환 함수
    public static UpdateMatchingPostData toUpdateMatchingPostData(UpdateMatchingPostRequest dto){
        UpdateMatchingPostData updateMatchingPostData = new UpdateMatchingPostData();
        updateMatchingPostData.setTitle(dto.getTitle());
        updateMatchingPostData.setContent(dto.getContent());
        updateMatchingPostData.setGender(dto.getGender());
        updateMatchingPostData.setParticipants(dto.getParticipants());
        updateMatchingPostData.setDesiredNumPeople(dto.getDesiredNumPeople());
        updateMatchingPostData.setOpenTalkLink(dto.getOpenTalkLink());
        return updateMatchingPostData;
    }
}
