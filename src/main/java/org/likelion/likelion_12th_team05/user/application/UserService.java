package org.likelion.likelion_12th_team05.user.application;

import lombok.extern.slf4j.Slf4j;
import org.likelion.likelion_12th_team05.global.auth.jwt.JwtTokenProvider;
import org.likelion.likelion_12th_team05.user.api.dto.request.UserSignInReqDto;
import org.likelion.likelion_12th_team05.user.api.dto.request.UserSignUpReqDto;
import org.likelion.likelion_12th_team05.user.api.dto.response.UserSignInResDto;
import org.likelion.likelion_12th_team05.user.domain.Role;
import org.likelion.likelion_12th_team05.user.domain.User;
import org.likelion.likelion_12th_team05.user.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    // 회원가입
    @Transactional
    public void userSignUp(UserSignUpReqDto userSignUpReqDto) {
        if (userRepository.existsByEmail(userSignUpReqDto.email())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        User user = User.builder()
                .name(userSignUpReqDto.name())
                .email(userSignUpReqDto.email())
                .password(passwordEncoder.encode(userSignUpReqDto.password()))
                .role(Role.USER)
                .build();

        userRepository.save(user);
    }

    public UserSignInResDto userSignIn(UserSignInReqDto userSignUpReqDto) {
        User user = userRepository.findByEmail(userSignUpReqDto.email())
                .orElseThrow(() -> new IllegalArgumentException("이메일이나 패스워드가 일치하지 않습니다."));
        String token = tokenProvider.generateToken(user.getEmail());
//      String refreshToken = tokenProvider.refreshToken(user.getEmail());

        if (!passwordEncoder.matches(userSignUpReqDto.password(), user.getPassword())) {
            throw new IllegalArgumentException("이메일이나 패스워드가 일치하지 않습니다.");
        }

        return UserSignInResDto.of(user, token);
    }
}
