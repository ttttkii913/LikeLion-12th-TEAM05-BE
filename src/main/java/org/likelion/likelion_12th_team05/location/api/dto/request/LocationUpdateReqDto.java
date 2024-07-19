package org.likelion.likelion_12th_team05.location.api.dto.request;

import org.likelion.likelion_12th_team05.location.domain.Location;

public record LocationUpdateReqDto(
        String name,
        String description,
        String address
) {
    public Location toEntity(String locationImage) {
        return Location.builder()
                .name(name)
                .description(description)
                .address(address)
                .locationImage(locationImage)
                .build();
    }
}
