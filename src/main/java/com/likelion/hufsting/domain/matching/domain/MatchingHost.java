package com.likelion.hufsting.domain.matching.domain;

import com.likelion.hufsting.domain.Member.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.Objects;

@Entity
@Getter
@Table(name = "HUFSTING_HOSTS")
public class MatchingHost {
    @Id @GeneratedValue
    @Column(name = "MATCHING_HOST_iD")
    private Long id; // PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private MatchingPost matchingPost;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY) // 개발 완료 후 cascade 삭제
    @JoinColumn(name = "HOST_Id")
    private Member host;

    protected MatchingHost(){}
    public MatchingHost(MatchingPost matchingPost, Member host) {
        this.matchingPost = matchingPost;
        this.host = host;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        MatchingHost that = (MatchingHost) object;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
