package org.likelion.likelion_12th_team05.comment.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.likelion.likelion_12th_team05.comment.api.request.CommentSaveReqDto;
import org.likelion.likelion_12th_team05.comment.api.request.CommentUpdateReqDto;
import org.likelion.likelion_12th_team05.comment.api.response.CommentInfoResDto;
import org.likelion.likelion_12th_team05.comment.api.response.CommentListResDto;
import org.likelion.likelion_12th_team05.comment.appication.CommentService;
import org.likelion.likelion_12th_team05.common.error.SuccessCode;
import org.likelion.likelion_12th_team05.config.ApiResponseTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "인증된 사용자가 큐레이션에 댓글 달기", description = "인증된 사용자가 산책로 지도 페이지에서 큐레이션에 댓글을 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "401", description = "인증이 필요합니다.")
    })
    @PostMapping("/{curationId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponseTemplate<CommentInfoResDto> commentSave(@PathVariable("curationId") Long curationId
                                                        , @RequestBody @Valid CommentSaveReqDto commentSaveReqDto, Principal principal) {
        CommentInfoResDto commentInfoResDto = commentService.commentSave(curationId, commentSaveReqDto, principal);
        return ApiResponseTemplate.successResponse(commentInfoResDto, SuccessCode.COMMENT_SAVE_SUCCESS);
    }

    @Operation(summary = "인증된 사용자가 큐레이션에 단 댓글 수정", description = "인증된 사용자가 산책로 지도 페이지에서 큐레이션에 단 댓글을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "401", description = "인증이 필요합니다.")
    })
    @PatchMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseTemplate commentUpdate(@PathVariable("commentId") Long commentId,
                                             @RequestBody @Valid CommentUpdateReqDto commentUpdateReqDto, Principal principal) {
        commentService.commentUpdate(commentId, commentUpdateReqDto, principal);
        return ApiResponseTemplate.successWithNoContent(SuccessCode.COMMENT_UPDATE_SUCCESS);
    }

    @Operation(summary = "인증된 사용자가 큐레이션에 단 댓글 삭제", description = "인증된 사용자가 산책로 지도 페이지에서 큐레이션에 단 댓글을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "401", description = "인증이 필요합니다.")
    })
    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseTemplate commentDelete(@PathVariable("commentId") Long commentId, Principal principal) {
        commentService.commentDelete(commentId, principal);
        return ApiResponseTemplate.successWithNoContent(SuccessCode.COMMENT_DELETE_SUCCESS);
    }

    @Operation(summary = "모든 사용자가  큐레이션에 달린 댓글 전체 조회", description = "모든 사용자가 산책로 지도 페이지에서 큐레이션에 단 댓글을 모두 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
    })
    @GetMapping("{curationId}")
    public ApiResponseTemplate<CommentListResDto> commentFindAll(@PathVariable("curationId") Long curationId) {
        CommentListResDto commentListResDto = commentService.commentFindAll(curationId);
        return ApiResponseTemplate.successResponse(commentListResDto, SuccessCode.GET_SUCCESS);
    }
}
