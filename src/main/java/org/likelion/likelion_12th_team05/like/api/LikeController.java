package org.likelion.likelion_12th_team05.like.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "인증된 사용자가 큐레이션에 좋아요", description = "인증된 사용자가 큐레이션에 좋아요를 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "401", description = "인증이 필요합니다.")
    })
    @PostMapping("/{curationId}/like")
    public ApiResponseTemplate<SuccessCode> likeSave(@PathVariable("curationId") Long curationId,
                                                     Principal principal) {
        likeService.likeSave(curationId, principal);
        return ApiResponseTemplate.successWithNoContent(SuccessCode.LIKE_SAVE_SUCCESS);
    }

    @Operation(summary = "인증된 사용자가 큐레이션에 좋아요 취소", description = "인증된 사용자가 큐레이션에 좋아요를 취소합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "401", description = "인증이 필요합니다.")
    })
    @DeleteMapping("/{curationId}/unlike")
    public ApiResponseTemplate<SuccessCode> likeDelete(@PathVariable("curationId") Long curationId,
                                                       Principal principal) throws IOException {
        likeService.likeDelete(curationId, principal);
        return ApiResponseTemplate.successWithNoContent(SuccessCode.LIKE_DELETE_SUCCESS);
    }
}