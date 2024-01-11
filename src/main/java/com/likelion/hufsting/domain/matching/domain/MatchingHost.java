package com.likelion.hufsting.domain.matching.domain;

import com.likelion.hufsting.domain.profile.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "HUFSTING_HOSTS")
public class MatchingHost {
    protected MatchingHost(){}
    public MatchingHost(MatchingPost matchingPost, Member host) {
        this.matchingPost = matchingPost;
        this.host = host;
    }

    @Id @GeneratedValue
    @Column(name = "MATCHING_HOST_iD")
    private Long id; // PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private MatchingPost matchingPost;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY) // 개발 완료 후 cascade 삭제
    @JoinColumn(name = "HOST_Id")
    private Member host;
}
