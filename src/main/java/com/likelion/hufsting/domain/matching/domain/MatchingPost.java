package com.likelion.hufsting.domain.matching.domain;

import com.likelion.hufsting.domain.matching.dto.UpdateMatchingPostData;
import com.likelion.hufsting.domain.profile.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "HUFSTING_POSTS")
public class MatchingPost {
    protected MatchingPost(){}
    public MatchingPost(String title, String content, Gender gender,
                        int desiredNumPeople, String openTalkLink,
                        Member author, MatchingStatus matchingStatus
                        ) {
        this.title = title;
        this.content = content;
        this.gender = gender;
        this.desiredNumPeople = desiredNumPeople;
        this.openTalkLink = openTalkLink;
        this.author = author;
        this.matchingStatus = matchingStatus;
    }

    @Id @GeneratedValue
    @Column(name = "POST_ID")
    private Long id; // PK

    private String title; // 제목
    private String content; // 내용

    @Enumerated(EnumType.STRING)
    private Gender gender; // 성별, MALE, FEMALE

    private int desiredNumPeople; // 희망 매칭 인원

    private String openTalkLink; // 오픈톡 링크

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY) // 개발 완료 후 cascade 삭제
    @JoinColumn(name = "AUTHOR_ID")
    private Member author; // 작성자

    @Enumerated(EnumType.STRING)
    private MatchingStatus matchingStatus; // 매칭 상태, WAITING, COMPLETED

    @OneToMany(mappedBy = "matchingPost", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<MatchingHost> matchingHosts = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void matchingPostUpdate(UpdateMatchingPostData dto){
        if(dto.getTitle() != null) this.title = dto.getTitle();
        if(dto.getContent() != null) this.content = dto.getContent();
        if(dto.getGender() != null) this.gender = dto.getGender();
        if(dto.getParticipants() != null) this.desiredNumPeople = dto.getDesiredNumPeople();
        if(dto.getOpenTalkLink() != null) this.openTalkLink = dto.getOpenTalkLink();
    }

    // MatchingHost Add Function
    public void addHost(List<MatchingHost> hosts){
        this.matchingHosts.addAll(hosts);
    }

    // MatchingHost Remove Function
    public void removeHost(List<MatchingHost> hosts){

    }
}
