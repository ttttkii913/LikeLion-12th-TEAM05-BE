package org.likelion.likelion_12th_team05.user.domain;

import jakarta.persistence.*;
import lombok.*;
import org.likelion.likelion_12th_team05.comment.domain.Comment;
import org.likelion.likelion_12th_team05.curation.domain.Curation;
import org.likelion.likelion_12th_team05.like.domain.Like;
import org.likelion.likelion_12th_team05.location.domain.Location;
import org.likelion.likelion_12th_team05.user.api.dto.request.UserInfoUpdateReqDto;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;

    private String accessToken;
    private String refreshToken;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Curation> curations = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Location> locations = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    @Column(name = "curation_count")
    private Integer curationCount = 0;

    @Column(name = "comment_count")
    private Integer commentCount = 0;

    @Builder
    private User(String name, String email, String password, String accessToken, String refreshToken, Role role){
        this.name = name;
        this.email = email;
        this.password = password;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.role = role;
    }

    public void update(UserInfoUpdateReqDto userInfoUpdateReqDto) {
        this.name = userInfoUpdateReqDto.name();
        this.email = userInfoUpdateReqDto.email();
        // 구글 로그인은 password가 null이라 따로 처리해줘야함
        if (userInfoUpdateReqDto.password() != null & !userInfoUpdateReqDto.email().isEmpty())
            this.password = userInfoUpdateReqDto.password();
    }
}
