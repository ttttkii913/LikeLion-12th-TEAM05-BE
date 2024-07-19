package org.likelion.likelion_12th_team05.user.api.dto;

import jakarta.validation.Valid;
import org.likelion.likelion_12th_team05.global.auth.template.RspTemplate;
import org.likelion.likelion_12th_team05.user.api.dto.request.UserSignInReqDto;
import org.likelion.likelion_12th_team05.user.api.dto.request.UserSignUpReqDto;
import org.likelion.likelion_12th_team05.user.api.dto.response.UserSignInResDto;
import org.likelion.likelion_12th_team05.user.application.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 자체 회원가입
    @PostMapping("/sign-up")
    public RspTemplate<String> userSignUp(@RequestBody @Valid UserSignUpReqDto userSignUpReqDto) {
        userService.userSignUp(userSignUpReqDto);
        return new RspTemplate<>(HttpStatus.CREATED, "회원가입");
    }

    // 자체 로그인
    @GetMapping("/sign-in")
    private RspTemplate<UserSignInResDto> userSignIn(@RequestBody @Valid UserSignInReqDto userSignInReqDto) {
        UserSignInResDto userSignInResDto = userService.userSignIn(userSignInReqDto);
        return new RspTemplate<>(HttpStatus.OK, "로그인", userSignInResDto);
    }
}
