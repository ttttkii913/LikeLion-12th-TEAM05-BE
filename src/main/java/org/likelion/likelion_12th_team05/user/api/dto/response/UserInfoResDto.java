package org.likelion.likelion_12th_team05.user.api.dto.response;

import lombok.Builder;
import org.likelion.likelion_12th_team05.user.domain.User;

// 유저 정보 수정시
@Builder
public record UserInfoResDto(
        Long userId,
        String name,
        String email
) {
    public static UserInfoResDto from(User user) {
        return UserInfoResDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .userId(user.getId())
                .build();
    }
}
