package com.likelion.hufsting.domain.Member.domain;

import com.likelion.hufsting.domain.profile.domain.Profile;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "MEMBER_NAME")
    private String name;

    @Column(name = "MEMBER_MAJOR")
    private String major;

    @Column(name = "MEMBER_EMAIL")
    private String email;

    @Column(name = "MEMBER_PWD")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "MEMBER_ROLE")
    private Role role;

    @Column(name = "PROFILE_SET_UP_STATUS")
    private Boolean profileSetUpStatus;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "PROFILE_ID")
    private Profile profile;
}
