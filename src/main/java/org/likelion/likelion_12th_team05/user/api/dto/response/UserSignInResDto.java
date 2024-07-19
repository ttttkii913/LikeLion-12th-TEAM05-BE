package org.likelion.likelion_12th_team05.user.api.dto.response;

import lombok.Builder;
import org.likelion.likelion_12th_team05.user.domain.User;

@Builder
public record UserSignInResDto(String name, String email, String token) {
    public static UserSignInResDto of(User user, String token) {
        return UserSignInResDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .token(token).build();
    }
}
