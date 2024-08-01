package org.likelion.likelion_12th_team05.location.api.dto.request;

import org.likelion.likelion_12th_team05.curation.domain.Curation;
import org.likelion.likelion_12th_team05.location.domain.Location;
import org.likelion.likelion_12th_team05.user.domain.User;

public record LocationSaveReqDto(
        String name,
        String description,
        String address,
        Double longitude,
        Double latitude
) {
    public Location toEntity(String locationImage, Curation curation, User user) {
        // 위치 저장 시 이미지를 선택으로 수정/ 이미지가 비어있지 않다면 이미지 저장, 비어있다면 빈 문자열로 locationImage 저장 => location 엔티티에서 locationImage nullable 지워줌
        String image = (locationImage != null) ? locationImage : "";

        return Location.builder()
                .name(name)
                .description(description)
                .address(address)
                .locationImage(image)
                .curation(curation)
                .user(user)
                .longitude(longitude)
                .latitude(latitude)
                .build();
    }
}
