package org.likelion.likelion_12th_team05.global.auth.kakaoAuth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.likelion.likelion_12th_team05.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class KaKaoTokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private final Key key;
    private final long accessTokenValidityTime; // 토큰 유효 시간

    @Autowired
    public KaKaoTokenProvider(@Value("${jwt.secret}") String secretKey,
                         @Value("${jwt.access-token-validity-in-milliseconds}") long accessTokenValidityTime) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);   // secretKey를 Base64 디코딩하여 바이트 배열로 변환
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenValidityTime = accessTokenValidityTime;
    }

    // 정보와 시크릿 키, 시간을 넣어 압축해 JWT 토큰 생성
    public KakaoToken generateToken(User user) {
        long nowTime = (new Date()).getTime();
        // 토큰 만료 시간 설정
        Date tokenExpiredTime = new Date(nowTime + accessTokenValidityTime);
        // 토큰 생성
        String accessToken = Jwts.builder()
                .setSubject(user.getEmail())    // userEmail을 주체로 설정
                .claim(AUTHORITIES_KEY, user.getRole().name())  // 사용자 권한(role)을 claim 객체에 추가
                .setExpiration(tokenExpiredTime)    // 만료 시간 설정
                .signWith(key, SignatureAlgorithm.HS256)    // 키, 서명 알고리즘
                .compact(); // 압축
        // 토큰 리턴
        return KakaoToken.builder()
                .accessToken(accessToken)
                .build();
    }
}
