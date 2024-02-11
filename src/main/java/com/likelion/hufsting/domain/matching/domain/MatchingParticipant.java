package com.likelion.hufsting.domain.matching.domain;

import com.likelion.hufsting.domain.Member.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Table(name = "HUFSTING_PARTICIPANTS")
public class MatchingParticipant {
    @Id @GeneratedValue
    @Column(name = "MATCHING_PART_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "REQ_ID")
    private MatchingRequest matchingRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARTICIPANT_ID")
    private Member participant;

    // Generator
    protected MatchingParticipant(){}
    public MatchingParticipant(MatchingRequest matchingRequest, Member participant){
        this.matchingRequest = matchingRequest;
        this.participant = participant;
    }
}
