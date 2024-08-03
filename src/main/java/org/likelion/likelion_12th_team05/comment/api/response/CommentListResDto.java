package org.likelion.likelion_12th_team05.comment.api.response;

import lombok.Builder;
import java.util.List;

@Builder
public record CommentListResDto(
        List<CommentInfoResDto> comments
) {
    public static CommentListResDto from(List<CommentInfoResDto> comments) {
        return CommentListResDto.builder()
                .comments(comments)
                .build();
    }
}
