package org.likelion.likelion_12th_team05.user.api.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record UserPopularListResDto(
        List<UserPopularInfoResDto> userPopular
) {
    public static UserPopularListResDto from(List<UserPopularInfoResDto> userPopular) {
        return UserPopularListResDto.builder()
                .userPopular(userPopular)
                .build();
    }
}
