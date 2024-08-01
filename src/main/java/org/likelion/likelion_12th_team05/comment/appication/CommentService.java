package org.likelion.likelion_12th_team05.comment.appication;

import org.likelion.likelion_12th_team05.comment.api.request.CommentSaveReqDto;
import org.likelion.likelion_12th_team05.comment.api.request.CommentUpdateReqDto;
import org.likelion.likelion_12th_team05.comment.api.response.CommentInfoResDto;
import org.likelion.likelion_12th_team05.comment.domain.Comment;
import org.likelion.likelion_12th_team05.comment.domain.CommentRepository;
import org.likelion.likelion_12th_team05.common.EntityFinder;
import org.likelion.likelion_12th_team05.common.error.ErrorCode;
import org.likelion.likelion_12th_team05.common.exception.CustomException;
import org.likelion.likelion_12th_team05.common.exception.NotFoundException;
import org.likelion.likelion_12th_team05.curation.domain.repository.CurationRepository;
import org.likelion.likelion_12th_team05.user.domain.User;
import org.likelion.likelion_12th_team05.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, CurationRepository curationRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public CommentInfoResDto commentSave(CommentSaveReqDto commentSaveReqDto, Principal principal) {
        String email = principal.getName();
        User user = getUserByEmail(email);

        Comment comment = commentSaveReqDto.toEntity(user);
        user.setCommentCount(user.getCommentCount() + 1);
        commentRepository.save(comment);

        return CommentInfoResDto.from(comment);
    }

    @Transactional
    public CommentInfoResDto commentUpdate(Long curationId, CommentUpdateReqDto commentUpdateReqDto, Principal principal) {
        String email = principal.getName();
        User user = getUserByEmail(email);

        Comment comment = getCommentById(curationId);

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
        Comment comment = getCommentById(commentId);

        String email = principal.getName();
        User user = getUserByEmail(email);
        String commentUserEmail = comment.getUser().getEmail();

        if (!email.equals(commentUserEmail)) {
            throw new NotFoundException(ErrorCode.USER_NOT_FOUND_EXCEPTION,
                    ErrorCode.USER_NOT_FOUND_EXCEPTION.getMessage());
        }

        user.setCommentCount(user.getCommentCount() - 1);
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
}
