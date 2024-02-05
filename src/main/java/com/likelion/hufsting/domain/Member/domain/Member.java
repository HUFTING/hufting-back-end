package com.likelion.hufsting.domain.Member.domain;

import com.likelion.hufsting.domain.follow.domain.Follow;
import com.likelion.hufsting.domain.profile.domain.Profile;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Handler;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

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

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "member")
    private Profile profile;

    @OneToMany(mappedBy = "follower",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private final List<Follow> followerList = new ArrayList<>();

    @OneToMany(mappedBy = "followee",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private final List<Follow> followeeList = new ArrayList<>();

    public void changeProfileSetUpStatus(){
        if(this.profileSetUpStatus.equals(Boolean.FALSE)){
            this.profileSetUpStatus = Boolean.TRUE;
        }else{
            this.profileSetUpStatus = Boolean.FALSE;
        }
    }

    public void setUpProfile(Profile profile){
        this.profile = profile;
    }
}
