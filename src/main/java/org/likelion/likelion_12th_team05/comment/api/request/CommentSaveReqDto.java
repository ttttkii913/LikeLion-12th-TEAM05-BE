package org.likelion.likelion_12th_team05.comment.api.request;

import jakarta.validation.constraints.NotBlank;
import org.likelion.likelion_12th_team05.curation.domain.Curation;

public record CommentSaveReqDto(
        Long id,

        @NotBlank
        String name,

        @NotBlank(message = "내용을 입력하여 주십시오")
        String comment,

        Curation curation
) {
}
