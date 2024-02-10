package com.likelion.hufsting.domain.matching.domain;

import com.likelion.hufsting.domain.Member.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "HUFSTING_REQ")
public class MatchingRequest {
    @Id @GeneratedValue
    @Column(name = "MATCHING_REQ_ID")
    private Long id;

    @Column(name = "MATCHING_REQ_TITLE")
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private MatchingPost matchingPost;

    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY) // 개발 완료 후 영속성 전이 제거 필요
    @JoinColumn(name = "REPRESENTATIVE_ID")
    private Member representative;

    @OneToMany(mappedBy = "matchingRequest", orphanRemoval = true)
    @Builder.Default
    private List<MatchingParticipant> participants = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private MatchingAcceptance matchingAcceptance; // ACCEPTED, REJECTED, WAITING

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchingRequest that = (MatchingRequest) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void updateTitle(String title){
        this.title = title;
    }

    // MatchingRequest add participants
    public void addParticipant(List<MatchingParticipant> reqParticipants){
        participants.addAll(reqParticipants);
    }

    // MatchingRequest update participants
    public void updateParticipant(List<MatchingParticipant> reqParticipants){
        // add new participant
        for(MatchingParticipant reqParticipant : reqParticipants){
            if(!participants.contains(reqParticipant)){
                participants.add(reqParticipant);
            }
        }
        // remove participant not in participants
        participants.removeIf(participant -> !reqParticipants.contains(participant));
    }

    // accept matchingRequest
    public void acceptMatchingRequest(){
        matchingAcceptance = MatchingAcceptance.ACCEPTED;
    }

    // reject matchingRequest
    public void rejectMatchingRequest(){
        matchingAcceptance = MatchingAcceptance.REJECTED;
    }
}
