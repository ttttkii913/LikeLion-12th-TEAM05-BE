package org.likelion.likelion_12th_team05.user.api;

import jakarta.validation.Valid;
import org.likelion.likelion_12th_team05.common.error.SuccessCode;
import org.likelion.likelion_12th_team05.config.ApiResponseTemplate;
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
    public ApiResponseTemplate<SuccessCode> userSignUp(@RequestBody @Valid UserSignUpReqDto userSignUpReqDto) {
        userService.userSignUp(userSignUpReqDto);
        return ApiResponseTemplate.successWithNoContent(SuccessCode.USER_SIGNUP_SUCCESS);
    }

    // 자체 로그인
    @GetMapping("/sign-in")
    private ApiResponseTemplate<UserSignInResDto> userSignIn(@RequestBody @Valid UserSignInReqDto userSignInReqDto) {
        UserSignInResDto userSignInResDto = userService.userSignIn(userSignInReqDto);
        return ApiResponseTemplate.successResponse(userSignInResDto, SuccessCode.USER_LOGIN_SUCCESS);
    }
}
