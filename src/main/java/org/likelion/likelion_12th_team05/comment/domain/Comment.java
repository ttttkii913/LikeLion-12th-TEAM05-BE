package org.likelion.likelion_12th_team05.comment.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.likelion.likelion_12th_team05.comment.api.request.CommentUpdateReqDto;
import org.likelion.likelion_12th_team05.curation.domain.Curation;
import org.likelion.likelion_12th_team05.user.domain.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curation_id")
    private Curation curation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "comment_count")
    private Integer commentCount = 0;

    @Builder
    private Comment(String comment, Curation curation, User user) {
        this.comment = comment;
        this.curation = curation;
        this.user = user;
    }

    public void updateCommentInfo(CommentUpdateReqDto commentUpdateReqDto, User user) {
        this.comment = commentUpdateReqDto.comment();
        this.user = user;
    }

    public void saveCommentCount() {
        this.commentCount++;
    }

    public void deleteComment() {
        if (this.commentCount > 0)
            this.commentCount--;
    }
}
