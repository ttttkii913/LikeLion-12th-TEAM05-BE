package org.likelion.likelion_12th_team05.comment.appication;

import org.likelion.likelion_12th_team05.comment.api.request.CommentUpdateReqDto;
import org.likelion.likelion_12th_team05.comment.api.response.CommentInfoResDto;
import org.likelion.likelion_12th_team05.comment.domain.Comment;
import org.likelion.likelion_12th_team05.comment.domain.CommentRepository;
import org.likelion.likelion_12th_team05.common.EntityFinder;
import org.likelion.likelion_12th_team05.common.error.ErrorCode;
import org.likelion.likelion_12th_team05.common.exception.CustomException;
import org.likelion.likelion_12th_team05.curation.api.dto.request.CurationSaveReqDto;
import org.likelion.likelion_12th_team05.curation.api.dto.response.CurationInfoResDto;
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
    public CurationInfoResDto commentSave(CurationSaveReqDto curationSaveReqDto, Principal principal) {
        String email = principal.getName();
        User user = getUserByEmail(email);

        Curation curation = curationSaveReqDto.toEntity(user);
        curation.setCommentCount(curation.getCommentCount() + 1);
        curationRepository.save(curation);

        return CurationInfoResDto.from(curation);
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
        String email = principal.getName();
        User user = getUserByEmail(email);

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new CustomException(ErrorCode.CURATIONS_NOT_FOUND_EXCEPTION,
                        ErrorCode.USER_NOT_FOUND_EXCEPTION.getMessage()));

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
