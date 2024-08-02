package org.likelion.likelion_12th_team05.global.auth.kakaoAuth;

import lombok.Data;

@Data
public class KakaoUserInfo {
    private Long id;
    private KakaoAccount kakao_account;
    private Properties properties;

    @Data
    public static class KakaoAccount {
        private String email;
    }

    @Data
    public static class Properties {
        private String nickname;
    }
}
