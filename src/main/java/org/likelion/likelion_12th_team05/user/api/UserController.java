package org.likelion.likelion_12th_team05.user.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.likelion.likelion_12th_team05.common.error.SuccessCode;
import org.likelion.likelion_12th_team05.config.ApiResponseTemplate;
import org.likelion.likelion_12th_team05.global.auth.googleAuth.AuthLoginService;
import org.likelion.likelion_12th_team05.global.auth.googleAuth.GoogleToken;
import org.likelion.likelion_12th_team05.global.auth.kakaoAuth.KakaoLoginService;
import org.likelion.likelion_12th_team05.global.auth.kakaoAuth.KakaoToken;
import org.likelion.likelion_12th_team05.user.api.dto.request.UserInfoUpdateReqDto;
import org.likelion.likelion_12th_team05.user.api.dto.request.UserSignInReqDto;
import org.likelion.likelion_12th_team05.user.api.dto.request.UserSignUpReqDto;
import org.likelion.likelion_12th_team05.user.api.dto.response.UserInfoResDto;
import org.likelion.likelion_12th_team05.user.api.dto.response.UserPopularListResDto;
import org.likelion.likelion_12th_team05.user.api.dto.response.UserSignInResDto;
import org.likelion.likelion_12th_team05.user.application.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class UserController {
    private final UserService userService;
    private final AuthLoginService authLoginService;
    private final KakaoLoginService kakaoLoginService;

    public UserController(UserService userService, AuthLoginService authLoginService, KakaoLoginService kakaoLoginService) {
        this.userService = userService;
        this.authLoginService = authLoginService;
        this.kakaoLoginService = kakaoLoginService;
    }

    @Operation(summary = "회원가입", description = "자체 회원가입")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "401", description = "인증이 필요합니다.")
    })
    @PostMapping("/sign-up")
    public ApiResponseTemplate<String> userSignUp(@RequestBody @Valid UserSignUpReqDto userSignUpReqDto, String email) {
        userService.userSignUp(userSignUpReqDto, email);
        return ApiResponseTemplate.successWithNoContent(SuccessCode.USER_SIGNUP_SUCCESS);
    }

    @Operation(summary = "구글 로그인", description = "구글 로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "401", description = "인증이 필요합니다.")
    })
    @GetMapping("/login")
    public GoogleToken googleCallback(@RequestParam(name = "code") String code) {
        String googleAccessToken = authLoginService.getGoogleAccessToken(code);
        return signUpOrSignIn(googleAccessToken);
    }

    public GoogleToken signUpOrSignIn(String googleAccessToken) {
        return authLoginService.signUpOrSignIn(googleAccessToken);
    }

    @Operation(summary = "카카오 인가 코드 발급 후 액세스 토큰 리다이렉트", description = "카카오 인가 코드 발급 후 액세스 토큰을 리다이렉트 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "401", description = "인증이 필요합니다.")
    })
    @GetMapping("/login/oauth2/code/kakao")
    public KakaoToken kakaoCallback(@RequestParam(name = "code") String code) {
        String kakaoAccessToken = kakaoLoginService.getKakaoAccessToken(code);
        return signUpOrSignInWithKakao(kakaoAccessToken);
    }

    public KakaoToken signUpOrSignInWithKakao(String kakaoAccessToken) {
        return kakaoLoginService.signUpOrSignInWithKakao(kakaoAccessToken);
    }

    @Operation(summary = "로그인", description = "자체 로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "401", description = "인증이 필요합니다.")
    })
    @PostMapping("/sign-in")
    private ApiResponseTemplate<UserSignInResDto> userSignIn(@RequestBody @Valid UserSignInReqDto userSignInReqDto) {
        UserSignInResDto userSignInResDto = userService.userSignIn(userSignInReqDto);
        return ApiResponseTemplate.successResponse(userSignInResDto, SuccessCode.USER_LOGIN_SUCCESS);
    }

    @Operation(summary = "사용자 정보 수정", description = "사용자의 정보(이름, 이메일, 비밀번호)를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "401", description = "인증이 필요합니다.")
    })
    @PatchMapping("/user/info")
    public ApiResponseTemplate<UserInfoResDto> userInfoUpdate(@RequestBody @Valid UserInfoUpdateReqDto userInfoUpdateReqDto, Principal principal) {
        UserInfoResDto userInfoResDto = userService.userInfoUpdate(userInfoUpdateReqDto, principal);
        return ApiResponseTemplate.successResponse(userInfoResDto, SuccessCode.USER_INFO_UPDATE_SUCCESS);
    }

    @Operation(summary = "모든 사용자가 큐레이션을 가장 많이 작성한 6명의 큐레이터 조회", description = "모든 사용자는 랜딩 페이지에서 큐레이션을 가장 많이 작성한 6명의 큐레이터를 볼 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "401", description = "인증이 필요합니다.")
    })
    @GetMapping("/user/popular")
    public ApiResponseTemplate<UserPopularListResDto> findAllByOrderByCurationCountDesc(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "6") int size,
            @RequestParam(value = "sort", defaultValue = "id,asc") String sort
    ) {
        String[] sortParams = sort.split(",");
        Sort sortOrder = Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]);
        Pageable pageable = PageRequest.of(page, size, sortOrder);

        UserPopularListResDto userPopularListResDto = userService.findAllByOrderByCurationCountDesc(pageable);
        return ApiResponseTemplate.successResponse(userPopularListResDto, SuccessCode.GET_SUCCESS);
    }

}
