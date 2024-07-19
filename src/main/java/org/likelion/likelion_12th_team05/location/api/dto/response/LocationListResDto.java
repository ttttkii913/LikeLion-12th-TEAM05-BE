package org.likelion.likelion_12th_team05.location.api.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record LocationListResDto(
        List<LocationInfoResDto> locations
) {
    public static LocationListResDto from(List<LocationInfoResDto> locations) {
        return LocationListResDto.builder()
                .locations(locations)
                .build();
    }
}
