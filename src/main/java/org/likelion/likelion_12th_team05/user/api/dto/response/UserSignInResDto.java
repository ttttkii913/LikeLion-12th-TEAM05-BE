package org.likelion.likelion_12th_team05.user.api.dto.response;

import lombok.Builder;
import org.likelion.likelion_12th_team05.user.domain.User;

@Builder
public record UserSignInResDto(String name, String email, String accessToken, String refreshToken) {
    public static UserSignInResDto of(User user, String accessToken, String refreshToken) {
        return UserSignInResDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken).build();
    }
}
