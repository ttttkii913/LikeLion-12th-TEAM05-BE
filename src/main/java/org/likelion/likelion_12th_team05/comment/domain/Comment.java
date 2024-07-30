package org.likelion.likelion_12th_team05.comment.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.likelion.likelion_12th_team05.comment.api.request.CommentUpdateReqDto;
import org.likelion.likelion_12th_team05.curation.domain.Curation;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curation_id")
    private Curation curation;

    @Builder
    private Comment(Long id, String name, String comment, Curation curation) {
        this.id = id;
        this.name = name;
        this.comment = comment;
        this.curation = curation;
    }

    public void updateCommentInfo(CommentUpdateReqDto commentUpdateReqDto) {
        this.comment = commentUpdateReqDto.comment();
    }
}
