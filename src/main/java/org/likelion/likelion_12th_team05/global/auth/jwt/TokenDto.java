package org.likelion.likelion_12th_team05.global.auth.jwt;

import lombok.Builder;

@Builder
public record TokenDto(
        String grantType,

        String accessToken,

        String refreshToken
) {
}
