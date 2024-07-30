package org.likelion.likelion_12th_team05.comment.api;

import jakarta.validation.Valid;
import org.likelion.likelion_12th_team05.comment.api.request.CommentSaveReqDto;
import org.likelion.likelion_12th_team05.comment.api.request.CommentUpdateReqDto;
import org.likelion.likelion_12th_team05.comment.appication.CommentService;
import org.likelion.likelion_12th_team05.common.error.SuccessCode;
import org.likelion.likelion_12th_team05.config.ApiResponseTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 저장
    @PostMapping
    public ApiResponseTemplate commentSave(@RequestBody @Valid CommentSaveReqDto commentSaveReqDto) {
        commentService.commentSave(commentSaveReqDto);
        return ApiResponseTemplate.successWithNoContent(SuccessCode.COMMENT_SAVE_SUCCESS);
    }

    // 수정
    @PatchMapping("/{commentId}")
    public ApiResponseTemplate commentUpdate(@PathVariable("commentId") Long curationId,
                                             @RequestBody @Valid CommentUpdateReqDto commentUpdateReqDto) {
        commentService.commentUpdate(curationId, commentUpdateReqDto);
        return ApiResponseTemplate.successWithNoContent(SuccessCode.COMMENT_UPDATE_SUCCESS);
    }

    // 삭제
    @DeleteMapping("/{commentId}")
    public ApiResponseTemplate commentDelete(@PathVariable("commentId") Long curationId) {
        commentService.commentDelete(curationId);
        return ApiResponseTemplate.successWithNoContent(SuccessCode.COMMENT_DELETE_SUCCESS);
    }
}
