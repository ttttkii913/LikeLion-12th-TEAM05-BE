package org.likelion.likelion_12th_team05.global.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.likelion.likelion_12th_team05.user.domain.User;
import org.likelion.likelion_12th_team05.user.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class JwtTokenProvider {
    private final UserRepository userRepository;

    @Value("${jwt.access-token-validity-in-milliseconds}")
    private String tokenExpireTime;

    @Value("${jwt.secret}")
    private String secret;
    private Key key;

    public JwtTokenProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        byte[] key = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(key);
    }

    // User 정보를 가지고 AccessToken, RefreshToken 을 생성하는 메소드
    public String generateToken(String email) {
        Date date = new Date();

        Date expireDate = new Date(date.getTime() + Long.parseLong(tokenExpireTime));

        String accessToken = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(date)
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return accessToken;
    }

    public String refreshToken(String email) {
        Date date = new Date();

        Date expireDate = new Date(date.getTime() + Long.parseLong(tokenExpireTime) * 24 * 7);

        String refreshToken = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(date)
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return refreshToken;
    }

    public boolean validateToken(String token) {
        // 토큰을 검증하는 부분
        try {
            Jwts.parserBuilder() // 토큰을 정수형으로 변환
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            // 성공 시 true 반환
            return true;

            // 실패 시 반환하는 예외에 따라 다르게 실행됨
        } catch (UnsupportedJwtException | MalformedJwtException exception) {
            log.error("JWT 가 유효하지 않습니다.");
            throw new IllegalArgumentException("JWT 가 유효하지 않습니다");
        } catch (SignatureException exception) {
            log.error("JWT 서명 검증에 실패했습니다.");
            throw new IllegalArgumentException("JWT 서명 검증에 실패했습니다.");
        } catch (ExpiredJwtException exception) {
            log.error("JWT 가 만료되었습니다.");
            throw new IllegalArgumentException("JWT 가 만료되었습니다.");
        } catch (IllegalArgumentException exception) {
            log.error("JWT 가 null 이거나 비어있거나 공백만 있습니다.");
            throw new IllegalArgumentException("JWT 가 null 이거나 비어있거나 공백만 있습니다.");
        } catch (Exception exception) {
            log.error("JWT 검증에 실패했습니다.", exception);
            throw new IllegalArgumentException("JWT 검증에 실패했습니다.");
        }
    }

    // 접근을 인증하는 부분
    public Authentication getAuthentication(String token) {
        // 요청을 받는 부분
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        // 이메일을 입력받아 레포지토리에서 findBy 함수로 찾는다.
        User user = userRepository.findByEmail(claims.getSubject()).orElseThrow();
        // 권한이나 역할의 이름을 반환하는 메서드를 사용하여 member.getRole()를 문자열로 만들어 리턴함(배열)
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole().toString()));

        // 사용자의 이메일과 공백, authorities 를 반환함
        return new UsernamePasswordAuthenticationToken(user.getEmail(), "", authorities);
    }
}
