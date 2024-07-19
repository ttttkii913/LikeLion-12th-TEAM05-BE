package org.likelion.likelion_12th_team05.curation.api.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record CurationListResDto(
        List<CurationInfoResDto> curations
) {
    public static CurationListResDto from(List<CurationInfoResDto> curations) {
        return CurationListResDto.builder()
                .curations(curations)
                .build();
    }
}
