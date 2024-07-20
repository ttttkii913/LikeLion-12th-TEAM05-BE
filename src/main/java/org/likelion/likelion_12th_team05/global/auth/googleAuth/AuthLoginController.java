//package org.likelion.likelion_12th_team05.global.auth.googleAuth;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/login/oauth2")
//public class AuthLoginController {
//    private final AuthLoginService authLoginService;
//
//    @GetMapping("/code/google")
//    public GoogleToken googleCallback(@RequestParam(name = "code") String code) {
//        String googleAccessToken = authLoginService.getGoogleAccessToken(code);
//        return signUpOrSignIn(googleAccessToken);
//    }
//
//    public GoogleToken signUpOrSignIn(String googleAccessToken) {
//        return authLoginService.signUpOrSignIn(googleAccessToken);
//    }
//
//}
// 주석처리를 했는데 머지 하고 통합 시 문제 없을 경우 다음 pr에 제거할 예정. 해당 코드는 userSignUpReqDto로 넘어감.
