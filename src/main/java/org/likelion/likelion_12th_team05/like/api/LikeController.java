package org.likelion.likelion_12th_team05.like.api;

import lombok.RequiredArgsConstructor;
import org.likelion.likelion_12th_team05.common.error.SuccessCode;
import org.likelion.likelion_12th_team05.config.ApiResponseTemplate;
import org.likelion.likelion_12th_team05.like.application.LikeService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class LikeController {
    private final LikeService likeService;

    // 인증된 사용자 - 큐레이션에 좋아요 누르기
    @PostMapping("/{curationId}/like")
    public ApiResponseTemplate<SuccessCode> likeSave(@PathVariable("curationId") Long curationId,
                                                     Principal principal) {
        likeService.likeSave(curationId, principal);
        return ApiResponseTemplate.successWithNoContent(SuccessCode.LIKE_SAVE_SUCCESS);
    }

    // 인증된 사용자 - 큐레이션에 좋아요 취소
    @DeleteMapping("/{curationId}/unlike")
    public ApiResponseTemplate<SuccessCode> likeDelete(@PathVariable("curationId") Long curationId,
                                                       Principal principal) throws IOException {
        likeService.likeDelete(curationId, principal);
        return ApiResponseTemplate.successWithNoContent(SuccessCode.LIKE_DELETE_SUCCESS);
    }
}