package org.likelion.likelion_12th_team05.curation.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.likelion.likelion_12th_team05.comment.domain.Comment;
import org.likelion.likelion_12th_team05.common.BaseTimeEntity;
import org.likelion.likelion_12th_team05.curation.api.dto.request.CurationUpdateReqDto;
import org.likelion.likelion_12th_team05.like.domain.Like;
import org.likelion.likelion_12th_team05.location.domain.Location;
import org.likelion.likelion_12th_team05.user.domain.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Curation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "curation_id")
    private Long id;

    @Column(name = "curation_name", nullable = false)
    private String name;

    @Column(name = "curation_content", nullable = false)
    private String content;

    @Column(name = "like_count")
    private Integer likeCount = 0;

    @Column(name = "comment_count")
    private Integer commentCount = 0;

    @OneToMany(mappedBy = "curation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Location> locations = new ArrayList<>();

    @OneToMany(mappedBy = "curation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "curation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Curation(String name, String content, User user) {
        this.name = name;
        this.content = content;
        this.user = user;
    }

    public void update(CurationUpdateReqDto curationUpdateReqDto, User user) {
        this.name = curationUpdateReqDto.name();
        this.content = curationUpdateReqDto.content();
        this.user = user;
    }

    // 인증된 사용자 - 좋아요 개수 증가, 감소
    public void saveLikeCount() {
        this.likeCount++;
    }

    public void deleteLikeCount() {
        if (this.likeCount > 0)
            this.likeCount--;
    }
}
