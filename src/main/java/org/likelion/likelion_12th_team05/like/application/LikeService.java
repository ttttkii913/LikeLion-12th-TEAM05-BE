package org.likelion.likelion_12th_team05.like.application;

import lombok.RequiredArgsConstructor;
import org.likelion.likelion_12th_team05.common.error.ErrorCode;
import org.likelion.likelion_12th_team05.common.exception.NotFoundException;
import org.likelion.likelion_12th_team05.curation.domain.Curation;
import org.likelion.likelion_12th_team05.curation.domain.repository.CurationRepository;
import org.likelion.likelion_12th_team05.like.domain.Like;
import org.likelion.likelion_12th_team05.like.domain.repository.LikeRepository;
import org.likelion.likelion_12th_team05.user.domain.User;
import org.likelion.likelion_12th_team05.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final CurationRepository curationRepository;

    @Transactional
    public void likeSave(@PathVariable("curationId") Long curationId, Principal principal) {
        Curation curation = curationRepository.findById(curationId).orElseThrow(
                () -> new NotFoundException(ErrorCode.CURATIONS_NOT_FOUND_EXCEPTION,
                        ErrorCode.CURATIONS_NOT_FOUND_EXCEPTION.getMessage()));

        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException(ErrorCode.USER_NOT_FOUND_EXCEPTION,
                        ErrorCode.USER_NOT_FOUND_EXCEPTION.getMessage()));

        Like like = Like.of(curation, user);
        likeRepository.save(like);

        // curation의 like count 증가
        curation.saveLikeCount();
        curationRepository.save(curation);

    }

    @Transactional
    public void likeDelete(@PathVariable("curationId") Long curationId, Principal principal) throws IOException {
        Curation curation = curationRepository.findById(curationId).orElseThrow(
                () -> new NotFoundException(ErrorCode.CURATIONS_NOT_FOUND_EXCEPTION,
                        ErrorCode.CURATIONS_NOT_FOUND_EXCEPTION.getMessage()));
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException(ErrorCode.USER_NOT_FOUND_EXCEPTION,
                        ErrorCode.USER_NOT_FOUND_EXCEPTION.getMessage()));

        // 큐레이션에 좋아요가 존재하는지 확인
        List<Like> existingLikes = likeRepository.findByCurationAndUser(curation, user);
        if (existingLikes.isEmpty()) {
            throw new NotFoundException(ErrorCode.LIKE_NOT_FOUND_EXCEPTION,
                    ErrorCode.LIKE_NOT_FOUND_EXCEPTION.getMessage());
        }
        Like like = existingLikes.get(0);
        likeRepository.delete(like);

        // curation의 like count 감소
        curation.deleteLikeCount();
        curationRepository.save(curation);
    }
}
