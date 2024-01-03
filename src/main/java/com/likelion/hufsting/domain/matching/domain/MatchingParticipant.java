package com.likelion.hufsting.domain.matching.domain;

import com.likelion.hufsting.domain.profile.domain.Member;
import jakarta.persistence.*;

@Entity
@Table(name = "HUFSTING_PARTICIPANTS")
public class MatchingParticipant {
    @Id @GeneratedValue
    @Column(name = "MATCHING_PART_ID")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "REQ_ID")
    private MatchingRequest matchingRequest;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "PART_Id")
    private Member participant;
}
