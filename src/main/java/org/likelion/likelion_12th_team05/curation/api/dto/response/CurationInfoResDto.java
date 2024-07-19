package org.likelion.likelion_12th_team05.curation.api.dto.response;

import lombok.Builder;
import org.likelion.likelion_12th_team05.curation.domain.Curation;

import java.time.LocalDate;

@Builder
public record CurationInfoResDto(
        Long id,
        String name,
        String content
) {
    public static CurationInfoResDto from(Curation curation) {
        return CurationInfoResDto.builder()
                .id(curation.getId())
                .name(curation.getName())
                .content(curation.getContent())
                .build();
    }
}
