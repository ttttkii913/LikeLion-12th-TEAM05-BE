package org.likelion.likelion_12th_team05.comment.api.request;

import jakarta.validation.constraints.NotBlank;
import org.likelion.likelion_12th_team05.user.domain.User;

public record CommentUpdateReqDto(
        @NotBlank(message = "내용을 입력하여 주십시오.")
        String comment,
        User user
) {
}
