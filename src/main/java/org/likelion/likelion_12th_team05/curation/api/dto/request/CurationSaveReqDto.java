package org.likelion.likelion_12th_team05.curation.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.likelion.likelion_12th_team05.curation.domain.Curation;
import org.likelion.likelion_12th_team05.user.domain.User;

@Builder
public record CurationSaveReqDto(
        @NotBlank(message = "제목은 필수로 입력해야 합니다.")
        @Size(min = 2, max = 15, message = "2자 이상 15자 이하로 입력해야 합니다.")
        String name,

        @NotBlank(message = "내용은 필수로 입력해야 합니다.")
        @Size(min = 2, max = 15, message = "2자 이상 15자 이하로 입력해야 합니다.")
        String content

) {
    public Curation toEntity(User user) {
        return Curation.builder()
                .name(this.name)
                .content(this.content)
                .user(user)
                .build();
    }
}