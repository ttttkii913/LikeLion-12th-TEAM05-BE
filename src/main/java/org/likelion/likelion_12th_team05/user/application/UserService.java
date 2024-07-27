package org.likelion.likelion_12th_team05.user.application;

import lombok.extern.slf4j.Slf4j;
import org.likelion.likelion_12th_team05.common.EntityFinder;
import org.likelion.likelion_12th_team05.common.error.ErrorCode;
import org.likelion.likelion_12th_team05.global.auth.jwt.JwtTokenProvider;
import org.likelion.likelion_12th_team05.user.api.dto.request.UserInfoUpdateReqDto;
import org.likelion.likelion_12th_team05.user.api.dto.request.UserSignInReqDto;
import org.likelion.likelion_12th_team05.user.api.dto.request.UserSignUpReqDto;
import org.likelion.likelion_12th_team05.user.api.dto.response.UserInfoResDto;
import org.likelion.likelion_12th_team05.user.api.dto.response.UserSignInResDto;
import org.likelion.likelion_12th_team05.user.domain.Role;
import org.likelion.likelion_12th_team05.user.domain.User;
import org.likelion.likelion_12th_team05.user.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

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
    public void userSignUp(UserSignUpReqDto userSignUpReqDto, String email) {
        if (userRepository.existsByEmail(userSignUpReqDto.email())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        User user = User.builder()
                .name(userSignUpReqDto.name())
                .email(userSignUpReqDto.email())
                .password(passwordEncoder.encode(userSignUpReqDto.password()))
                .accessToken(tokenProvider.generateToken(email))
                .refreshToken(tokenProvider.refreshToken(email))
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user);
    }

    public UserSignInResDto userSignIn(UserSignInReqDto userSignUpReqDto) {
        User user = userRepository.findByEmail(userSignUpReqDto.email())
                .orElseThrow(() -> new IllegalArgumentException("이메일이나 패스워드가 일치하지 않습니다."));
        String accessToken = tokenProvider.generateToken(user.getEmail());
        String refreshToken = tokenProvider.refreshToken(user.getEmail());

        if (!passwordEncoder.matches(userSignUpReqDto.password(), user.getPassword())) {
            throw new IllegalArgumentException("이메일이나 패스워드가 일치하지 않습니다.");
        }

        return UserSignInResDto.of(user, accessToken, refreshToken);
    }

    @Transactional
    public UserInfoResDto userInfoUpdate(UserInfoUpdateReqDto userInfoUpdateReqDto, Principal principal) {
        String email = principal.getName();
        User user = getUserByEmail(email);

        user.update(userInfoUpdateReqDto);

        // 비밀번호 수정할 경우에도 암호화 설정이 들어가도록(없으면 user 테이블에서 비밀번호가 그대로 보임)
        if (userInfoUpdateReqDto.password() != null && !userInfoUpdateReqDto.password().isEmpty()) {
            String newEncodedPassword = passwordEncoder.encode(userInfoUpdateReqDto.password());
            user.setPassword(newEncodedPassword);
        }

        userRepository.save(user);
        return UserInfoResDto.from(user);
    }

    private User getUserByEmail(String email) {
        return EntityFinder.findByEmailOrThrow(userRepository.findByEmail(email)
                , ErrorCode.USER_NOT_FOUND_EXCEPTION);
    }
}
