package org.likelion.likelion_12th_team05.comment.api.response;

import lombok.Builder;
import org.likelion.likelion_12th_team05.comment.domain.Comment;

@Builder
public record CommentInfoResDto(String name, String comment) {
    public static CommentInfoResDto from (Comment comment) {
        return CommentInfoResDto.builder()
                .name(comment.getName())
                .comment(comment.getComment())
                .build();
    }
}
