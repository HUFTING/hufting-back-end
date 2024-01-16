package com.likelion.hufsting.domain.matching.domain;

import com.likelion.hufsting.domain.profile.domain.Member;
import jakarta.persistence.*;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Table(name = "HUFSTING_REQ")
public class MatchingRequest {
    @Id @GeneratedValue
    @Column(name = "MATCHING_REQ_ID")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private MatchingPost matchingPost;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "REPRESENTATIVE_ID")
    private Member representative;

    @OneToMany(mappedBy = "matchingRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatchingParticipant> participants;

    @Enumerated(EnumType.STRING)
    private MatchingAcceptance matchingAcceptance; // ACCEPTED, REJECTED, WAITING

    // MatchingRequest add participants
    public void addParticipant(List<MatchingParticipant> participants){
        this.participants.addAll(participants);
    }

    // MatchingRequest update participants
    public void updateParticipant(List<MatchingRequest> participants){
        // 코드 작성
    }
}
