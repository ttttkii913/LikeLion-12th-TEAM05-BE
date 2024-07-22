package org.likelion.likelion_12th_team05.like.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.likelion.likelion_12th_team05.curation.domain.Curation;
import org.likelion.likelion_12th_team05.user.domain.User;

@Getter
@Entity(name = "likes") // like가 예약어라 name 설정해줘야함
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "curation_id")
    private Curation curation;

    public static Like of(Curation curation, User user) {
        return Like.builder()
                .curation(curation)
                .user(user)
                .build();
    }
}
