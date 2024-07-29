package org.likelion.likelion_12th_team05.user.application;

import lombok.extern.slf4j.Slf4j;
import org.likelion.likelion_12th_team05.common.EntityFinder;
import org.likelion.likelion_12th_team05.common.error.ErrorCode;
import org.likelion.likelion_12th_team05.common.exception.CustomException;
import org.likelion.likelion_12th_team05.global.auth.jwt.JwtTokenProvider;
import org.likelion.likelion_12th_team05.user.api.dto.request.UserInfoUpdateReqDto;
import org.likelion.likelion_12th_team05.user.api.dto.request.UserSignInReqDto;
import org.likelion.likelion_12th_team05.user.api.dto.request.UserSignUpReqDto;
import org.likelion.likelion_12th_team05.user.api.dto.response.UserInfoResDto;
import org.likelion.likelion_12th_team05.user.api.dto.response.UserPopularInfoResDto;
import org.likelion.likelion_12th_team05.user.api.dto.response.UserPopularListResDto;
import org.likelion.likelion_12th_team05.user.api.dto.response.UserSignInResDto;
import org.likelion.likelion_12th_team05.user.domain.Role;
import org.likelion.likelion_12th_team05.user.domain.User;
import org.likelion.likelion_12th_team05.user.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

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
            throw new CustomException(ErrorCode.VALIDATION_ERROR, "이미 존재하는 이메일입니다.");
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
                .orElseThrow(() -> new CustomException(ErrorCode.VALIDATION_ERROR ,"이메일이나 패스워드가 일치하지 않습니다."));
        String accessToken = tokenProvider.generateToken(user.getEmail());
        String refreshToken = tokenProvider.refreshToken(user.getEmail());

        if (!passwordEncoder.matches(userSignUpReqDto.password(), user.getPassword())) {
            throw new CustomException(ErrorCode.VALIDATION_ERROR, "이메일이나 패스워드가 일치하지 않습니다.");
        }

        return UserSignInResDto.of(user, accessToken, refreshToken);
    }

    // 사용자 정보(이름, 이메일, 비밀번호) 수정
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

    // 랜딩 페이지 - 큐레이션 많이 쓴 사용자 6명(작성한 큐레이션 수, 이름) 조회
    @Transactional
    public UserPopularListResDto findByOrderByCurationsCurationCountDesc() {
        List<User> users = userRepository.findByOrderByCurationsCurationCountDesc();
        List<UserPopularInfoResDto> userPopularInfoResDtoList = users.stream()
                .map(UserPopularInfoResDto::from)
                .toList();
        return UserPopularListResDto.from(userPopularInfoResDtoList);
    }

    private User getUserByEmail(String email) {
        return EntityFinder.findByEmailOrThrow(userRepository.findByEmail(email)
                , ErrorCode.USER_NOT_FOUND_EXCEPTION);
    }
}
