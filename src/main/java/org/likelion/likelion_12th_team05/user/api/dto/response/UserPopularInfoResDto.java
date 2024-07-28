package org.likelion.likelion_12th_team05.user.api.dto.response;

import lombok.Builder;
import org.likelion.likelion_12th_team05.user.domain.User;

// 랜딩 페이지 - 큐레이션 많이 작성한 사용자 정보(이름, 큐레이션 수)
@Builder
public record UserPopularInfoResDto(
        String name,
        Integer curationCount
) {
    public static UserPopularInfoResDto from(User user) {
        return UserPopularInfoResDto.builder()
                .name(user.getName())
                .curationCount(user.getCurationCount())
                .build();
    }
}
