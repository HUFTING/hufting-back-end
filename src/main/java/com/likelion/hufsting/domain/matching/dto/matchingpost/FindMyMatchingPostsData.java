package com.likelion.hufsting.domain.matching.dto.matchingpost;

import com.likelion.hufsting.domain.matching.domain.MatchingPost;
import com.likelion.hufsting.domain.matching.domain.MatchingStatus;
import com.likelion.hufsting.global.domain.Gender;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class FindMyMatchingPostsData {
    // 매칭글 관련
    private Long id;
    private String title;
    private int desiredNumPeople;
    private Gender gender;
    private String authorName;
    private LocalDateTime createdAt;
    private MatchingStatus matchingStatus;

    // convert to FindMyMatchingPostData
    public static FindMyMatchingPostsData toFindMyMatchingPostData(MatchingPost matchingPost){
        FindMyMatchingPostsData myMatchingPostData = new FindMyMatchingPostsData();
        myMatchingPostData.setId(matchingPost.getId());
        myMatchingPostData.setTitle(matchingPost.getTitle());
        myMatchingPostData.setDesiredNumPeople(matchingPost.getDesiredNumPeople());
        myMatchingPostData.setGender(matchingPost.getGender());
        myMatchingPostData.setAuthorName(matchingPost.getAuthor().getName());
        myMatchingPostData.setCreatedAt(matchingPost.getCreatedAt());
        myMatchingPostData.setMatchingStatus(matchingPost.getMatchingStatus());
        return myMatchingPostData;
    }
}