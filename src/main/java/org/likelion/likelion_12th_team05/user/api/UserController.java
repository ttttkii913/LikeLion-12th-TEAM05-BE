package org.likelion.likelion_12th_team05.user.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.likelion.likelion_12th_team05.common.error.SuccessCode;
import org.likelion.likelion_12th_team05.config.ApiResponseTemplate;
import org.likelion.likelion_12th_team05.global.auth.googleAuth.AuthLoginService;
import org.likelion.likelion_12th_team05.global.auth.googleAuth.GoogleToken;
import org.likelion.likelion_12th_team05.global.auth.jwt.TokenDto;
import org.likelion.likelion_12th_team05.user.api.dto.request.UserSignInReqDto;
import org.likelion.likelion_12th_team05.user.api.dto.request.UserSignUpReqDto;
import org.likelion.likelion_12th_team05.user.api.dto.response.UserSignInResDto;
import org.likelion.likelion_12th_team05.user.application.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private final UserService userService;
    private final AuthLoginService authLoginService;

    public UserController(UserService userService, AuthLoginService authLoginService) {
        this.userService = userService;
        this.authLoginService = authLoginService;
    }

    @Operation(summary = "회원가입", description = "자체 회원가입")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "401", description = "인증이 필요합니다.")
    })
    @PostMapping("/sign-up")
    public ApiResponseTemplate<String> userSignUp(@RequestBody @Valid UserSignUpReqDto userSignUpReqDto, TokenDto tokenDto) {
        userService.userSignUp(userSignUpReqDto, tokenDto);
        return ApiResponseTemplate.successResponse(userSignUpReqDto.refreshToken(), SuccessCode.USER_SIGNUP_SUCCESS);
    }

    @Operation(summary = "구글 로그인", description = "구글 로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "401", description = "인증이 필요합니다.")
    })
    @GetMapping("/code/google")
    public GoogleToken googleCallback(@RequestParam(name = "code") String code) {
        String googleAccessToken = authLoginService.getGoogleAccessToken(code);
        return signUpOrSignIn(googleAccessToken);
    }

    public GoogleToken signUpOrSignIn(String googleAccessToken) {
        return authLoginService.signUpOrSignIn(googleAccessToken);
    }

    @Operation(summary = "로그인", description = "자체 로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "401", description = "인증이 필요합니다.")
    })
    @GetMapping("/sign-in")
    private ApiResponseTemplate<UserSignInResDto> userSignIn(@RequestBody @Valid UserSignInReqDto userSignInReqDto) {
        UserSignInResDto userSignInResDto = userService.userSignIn(userSignInReqDto);
        return ApiResponseTemplate.successResponse(userSignInResDto, SuccessCode.USER_LOGIN_SUCCESS);
    }
}
