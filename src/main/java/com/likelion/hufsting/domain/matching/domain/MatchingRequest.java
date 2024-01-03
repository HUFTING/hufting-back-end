package com.likelion.hufsting.domain.matching.domain;

import com.likelion.hufsting.domain.profile.domain.User;
import jakarta.persistence.*;

@Entity
@Table(name = "HUFSTING_REQ")
public class MatchingRequest {
    @Id @GeneratedValue
    @Column(name = "MATCHING_REQ_ID")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private MatchingPost matchingPost;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "PART_ID")
    private User participant;

    @Enumerated(EnumType.STRING)
    private MatchingAcceptance matchingAcceptance; // ACCEPTED, REJECTED
}
