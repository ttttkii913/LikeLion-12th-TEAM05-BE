package org.likelion.likelion_12th_team05.curation.api.dto.request;

import lombok.Builder;
import org.likelion.likelion_12th_team05.curation.domain.Curation;
import org.likelion.likelion_12th_team05.user.domain.User;

@Builder
public record CurationSaveReqDto(
        String name,
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