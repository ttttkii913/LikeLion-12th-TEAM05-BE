package org.likelion.likelion_12th_team05.user.api.dto.response;

import lombok.Builder;
import org.likelion.likelion_12th_team05.user.domain.User;

@Builder
public record UserSignInResDto(String name, String email, String refreshToken) {
    public static UserSignInResDto of(User user, String refreshToken) {
        return UserSignInResDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .refreshToken(refreshToken).build();
    }
}
