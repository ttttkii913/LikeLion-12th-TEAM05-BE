package org.likelion.likelion_12th_team05.curation.api.dto.request;

import lombok.Builder;
import org.likelion.likelion_12th_team05.curation.domain.Curation;
import org.likelion.likelion_12th_team05.location.domain.Location;

@Builder
public record CurationSaveReqDto(
        String name,
        String content

) {
    public Curation toEntity() {
        return Curation.builder()
                .name(this.name)
                .content(this.content)
                .build();
    }
}