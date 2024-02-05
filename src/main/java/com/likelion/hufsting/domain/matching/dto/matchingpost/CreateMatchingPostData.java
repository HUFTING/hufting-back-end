package com.likelion.hufsting.domain.matching.dto.matchingpost;

import com.likelion.hufsting.domain.matching.domain.Gender;
import lombok.Data;
import org.springframework.security.core.Authentication;

import java.util.List;

@Data
public class CreateMatchingPostData {
    private String title;
    private String content;
    private Gender gender;
    private int desiredNumPeople;
    private String openTalkLink;
    private List<Long> participants;

    // Controller-Service 간 DTO 변환 함수
    public static CreateMatchingPostData toCreateMatchingPostData(CreateMatchingPostRequest dto){
        CreateMatchingPostData createMatchingPostData = new CreateMatchingPostData();
        createMatchingPostData.setTitle(dto.getTitle());
        createMatchingPostData.setGender(dto.getGender());
        createMatchingPostData.setParticipants(dto.getParticipants());
        createMatchingPostData.setDesiredNumPeople(dto.getDesiredNumPeople());
        createMatchingPostData.setOpenTalkLink(dto.getOpenTalkLink());
        return createMatchingPostData;
    }

}
