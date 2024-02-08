package com.likelion.hufsting.domain.matching.dto.matchingpost;

import com.likelion.hufsting.domain.matching.domain.MatchingPost;
import com.likelion.hufsting.domain.matching.domain.MatchingStatus;
import com.likelion.hufsting.domain.matching.dto.matchingrequest.FindMatchingReqInPostData;
import lombok.Data;

import java.util.List;

@Data
public class FindMyMatchingPostsData {
    // 매칭글 관련
    private Long matchingPostId;
    private String matchingPostTitle;
    private MatchingStatus matchingStatus;
    // 매칭 요청 관련
    private int matchingRequestCount;
    private List<FindMatchingReqInPostData> matchingRequests;

    // convert to FindMyMatchingPostData
    public static FindMyMatchingPostsData toFindMyMatchingPostData(MatchingPost matchingPost){
        FindMyMatchingPostsData myMatchingPostData = new FindMyMatchingPostsData();
        myMatchingPostData.setMatchingPostId(matchingPost.getId());
        myMatchingPostData.setMatchingPostTitle(matchingPost.getTitle());
        myMatchingPostData.setMatchingStatus(matchingPost.getMatchingStatus());
        myMatchingPostData.setMatchingRequestCount(matchingPost.getMatchingRequests().size());
        // 매칭 요청 변환
       List<FindMatchingReqInPostData> findMatchingReqInPostDatas = matchingPost.getMatchingRequests().stream()
                .map(FindMatchingReqInPostData::toFindMatchingReqInPostData)
                .toList();
        myMatchingPostData.setMatchingRequests(findMatchingReqInPostDatas);
        return myMatchingPostData;
    }
}