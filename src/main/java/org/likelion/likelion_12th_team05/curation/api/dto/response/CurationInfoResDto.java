package org.likelion.likelion_12th_team05.curation.api.dto.response;

import lombok.Builder;
import org.likelion.likelion_12th_team05.curation.domain.Curation;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record CurationInfoResDto(
        Long id,
        String name,
        String content,
        LocalDateTime createDate,
        Integer likeCount
) {
    public static CurationInfoResDto from(Curation curation) {
        return CurationInfoResDto.builder()
                .id(curation.getId())
                .name(curation.getName())
                .content(curation.getContent())
                .createDate(curation.getCreateDate())
                .likeCount(curation.getLikeCount())
                .build();
    }
}
