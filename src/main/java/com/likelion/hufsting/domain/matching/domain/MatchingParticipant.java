package com.likelion.hufsting.domain.matching.domain;

import com.likelion.hufsting.domain.profile.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "HUFSTING_PARTICIPANTS")
public class MatchingParticipant {
    @Id @GeneratedValue
    @Column(name = "MATCHING_PART_ID")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "REQ_ID")
    private MatchingRequest matchingRequest;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "PARTICIPANT_Id")
    private Member participant;

    // Generator
    protected MatchingParticipant(){}
    public MatchingParticipant(MatchingRequest matchingRequest, Member participant){
        this.matchingRequest = matchingRequest;
        this.participant = participant;
    }
}
