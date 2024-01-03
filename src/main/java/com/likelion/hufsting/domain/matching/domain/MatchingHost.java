package com.likelion.hufsting.domain.matching.domain;

import com.likelion.hufsting.domain.profile.domain.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "HUFSTING_HOSTS")
public class MatchingHost {
    @Id @GeneratedValue
    @Column(name = "MATCHING_HOST_iD")
    private Long id; // PK

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private MatchingPost matchingPost;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "HOST_Id")
    private User host;
}
