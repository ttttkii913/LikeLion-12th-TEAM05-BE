package org.likelion.likelion_12th_team05.comment.api;

import jakarta.validation.Valid;
import org.likelion.likelion_12th_team05.comment.api.request.CommentSaveReqDto;
import org.likelion.likelion_12th_team05.comment.api.request.CommentUpdateReqDto;
import org.likelion.likelion_12th_team05.comment.api.response.CommentInfoResDto;
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

    // 저장
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponseTemplate<CommentInfoResDto> commentSave(@RequestBody @Valid CommentSaveReqDto commentSaveReqDto
            , Principal principal) {
        CommentInfoResDto commentInfoResDto = commentService.commentSave(commentSaveReqDto, principal);
        return ApiResponseTemplate.successResponse(commentInfoResDto, SuccessCode.COMMENT_SAVE_SUCCESS);
    }

    // 수정
    @PatchMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseTemplate commentUpdate(@PathVariable("commentId") Long curationId,
                                             @RequestBody @Valid CommentUpdateReqDto commentUpdateReqDto, Principal principal) {
        commentService.commentUpdate(curationId, commentUpdateReqDto, principal);
        return ApiResponseTemplate.successWithNoContent(SuccessCode.COMMENT_UPDATE_SUCCESS);
    }

    // 삭제
    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseTemplate commentDelete(@PathVariable("commentId") Long curationId, Principal principal) {
        commentService.commentDelete(curationId, principal);
        return ApiResponseTemplate.successWithNoContent(SuccessCode.COMMENT_DELETE_SUCCESS);
    }
}
