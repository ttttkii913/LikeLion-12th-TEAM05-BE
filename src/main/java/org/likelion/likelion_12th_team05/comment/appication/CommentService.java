package org.likelion.likelion_12th_team05.comment.appication;

import org.likelion.likelion_12th_team05.comment.api.request.CommentSaveReqDto;
import org.likelion.likelion_12th_team05.comment.api.request.CommentUpdateReqDto;
import org.likelion.likelion_12th_team05.comment.domain.Comment;
import org.likelion.likelion_12th_team05.comment.domain.CommentRepository;
import org.likelion.likelion_12th_team05.common.error.ErrorCode;
import org.likelion.likelion_12th_team05.common.exception.CustomException;
import org.likelion.likelion_12th_team05.curation.domain.Curation;
import org.likelion.likelion_12th_team05.curation.domain.repository.CurationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final CurationRepository curationRepository;

    public CommentService(CommentRepository commentRepository, CurationRepository curationRepository) {
        this.commentRepository = commentRepository;
        this.curationRepository = curationRepository;
    }

    public void commentSave(CommentSaveReqDto commentSaveReqDto) {
        Curation curation = curationRepository.findById(commentSaveReqDto.id())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND_EXCEPTION, "해당하는 사용자가 없습니다."));

        Comment comment = Comment.builder()
                .id(commentSaveReqDto.id())
                .name(commentSaveReqDto.name())
                .comment(commentSaveReqDto.comment())
                .curation(curation).build();

        commentRepository.save(comment);
    }

    public void commentUpdate(Long curationId, CommentUpdateReqDto commentUpdateReqDto) {
        Comment comment = commentRepository.findById(curationId)
                .orElseThrow(() -> new CustomException(ErrorCode.CURATIONS_NOT_FOUND_EXCEPTION, "해당하는 큐레이션이 없습니다."));

        comment.updateCommentInfo(commentUpdateReqDto);
    }

    public void commentDelete(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.CURATIONS_NOT_FOUND_EXCEPTION, "해당하는 큐레이션이 없습니다."));

        commentRepository.delete(comment);
    }
}
