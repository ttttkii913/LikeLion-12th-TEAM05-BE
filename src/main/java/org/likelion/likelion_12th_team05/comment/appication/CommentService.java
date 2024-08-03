package org.likelion.likelion_12th_team05.comment.appication;

import org.likelion.likelion_12th_team05.comment.api.request.CommentSaveReqDto;
import org.likelion.likelion_12th_team05.comment.api.request.CommentUpdateReqDto;
import org.likelion.likelion_12th_team05.comment.api.response.CommentInfoResDto;
import org.likelion.likelion_12th_team05.comment.domain.Comment;
import org.likelion.likelion_12th_team05.comment.domain.CommentRepository;
import org.likelion.likelion_12th_team05.common.EntityFinder;
import org.likelion.likelion_12th_team05.common.error.ErrorCode;
import org.likelion.likelion_12th_team05.common.exception.CustomException;
import org.likelion.likelion_12th_team05.curation.domain.Curation;
import org.likelion.likelion_12th_team05.curation.domain.repository.CurationRepository;
import org.likelion.likelion_12th_team05.user.domain.User;
import org.likelion.likelion_12th_team05.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final CurationRepository curationRepository;
    private final UserRepository userRepository;


    public CommentService(CommentRepository commentRepository, CurationRepository curationRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.curationRepository = curationRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public CommentInfoResDto commentSave(Long curationId, CommentSaveReqDto commentSaveReq, Principal principal) {
        Curation curation = getCurationById(curationId);    // 큐레이션 엔티티 조회 <- 큐레이션의 댓글 카운트 설정을 위함
        String email = principal.getName();
        User user = getUserByEmail(email);

        Comment comment = commentSaveReq.toEntity(user, curation);    // toEntity로 user 엔티티 정보와 함께 comment 객체 생성
        curation.setCommentCount(curation.getCommentCount() + 1);   // curation의 commentCount를 get 해와 + 1 한 다음에 setCommentCount로 세팅해줌
        commentRepository.save(comment);    // 댓글 저장

        return CommentInfoResDto.from(comment); // 댓글 정보 리턴
    }

    @Transactional
    public CommentInfoResDto commentUpdate(Long commentId, CommentUpdateReqDto commentUpdateReqDto, Principal principal) {
        String email = principal.getName();
        User user = getUserByEmail(email);

        Comment comment = getCommentById(commentId);

        String commentUserEmail = comment.getUser().getEmail();
        if (!email.equals(commentUserEmail)) {
            throw new CustomException(ErrorCode.NO_AUTHORIZATION_EXCEPTION,
                    ErrorCode.NO_AUTHORIZATION_EXCEPTION.getMessage());
        }

        comment.updateCommentInfo(commentUpdateReqDto, user);
        commentRepository.save(comment);
        return CommentInfoResDto.from(comment);
    }

    @Transactional
    public void commentDelete(Long commentId, Principal principal) {
        String email = principal.getName();

        Comment comment = getCommentById(commentId);

        String commentUserEmail = comment.getUser().getEmail();
        if (!email.equals(commentUserEmail)) {
            throw new CustomException(ErrorCode.NO_AUTHORIZATION_EXCEPTION,
                    ErrorCode.NO_AUTHORIZATION_EXCEPTION.getMessage());
        }

        Curation curation = comment.getCuration();
        if (curation != null)
            curation.setCommentCount(curation.getCommentCount() - 1);
        commentRepository.delete(comment);
    }

    private Comment getCommentById(Long commentId) {
        return EntityFinder.findByIdOrThrow
                (commentRepository.findById(commentId), ErrorCode.USER_NOT_FOUND_EXCEPTION);
    }

    private User getUserByEmail(String email) {
        return EntityFinder.findByEmailOrThrow
                (userRepository.findByEmail(email), ErrorCode.USER_NOT_FOUND_EXCEPTION);
    }

    private Curation getCurationById(Long curationId) {
        return EntityFinder.findByIdOrThrow(curationRepository.findById(curationId)
                , ErrorCode.CURATIONS_NOT_FOUND_EXCEPTION);
    }
}
