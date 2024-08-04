package org.likelion.likelion_12th_team05.like.application;

import lombok.RequiredArgsConstructor;
import org.likelion.likelion_12th_team05.common.EntityFinder;
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

import java.security.Principal;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final CurationRepository curationRepository;

    @Transactional
    public void likeSave(@PathVariable("curationId") Long curationId, Principal principal) {
        Curation curation = getCurationById(curationId);

        String email = principal.getName();
        User user = getUserByEmail(email);

        // 사용자가 이미 좋아요를 눌렀는지 확인 후 이미 눌렀다면 - 이미 누른 좋아요 입니다"
        // 안 눌렀다면 save되도록 수정
        if (likeRepository.findByCurationAndUser(curation, user).isPresent()) {
            throw new NotFoundException(ErrorCode.ALREADY_LIKE_CURATION
                    , ErrorCode.ALREADY_LIKE_CURATION.getMessage());
        }

        Like like = Like.of(curation, user);
        likeRepository.save(like);

        // curation의 like count 증가
        curation.saveLikeCount();
        curationRepository.save(curation);

    }

    @Transactional
    public void likeDelete(@PathVariable("curationId") Long curationId, Principal principal) {
        Curation curation = getCurationById(curationId);

        String email = principal.getName();
        User user = getUserByEmail(email);

        // 사용자가 좋아요를 누르지 않았을 때 -> likeCount가 이미 0일 때 또 삭제를 시도하면 좋아요를 누르지 않았다고 에러 줌
        Optional<Like> existingLikes = likeRepository.findByCurationAndUser(curation, user);
        if (existingLikes.isEmpty()) {
            throw new NotFoundException(ErrorCode.LIKE_NOT_FOUND_EXCEPTION,
                    ErrorCode.LIKE_NOT_FOUND_EXCEPTION.getMessage());
        }
        Like like = existingLikes.get();
        likeRepository.delete(like);

        // curation의 like count 감소
        curation.deleteLikeCount();
        curationRepository.save(curation);
    }

    // 반복되는 예외 반환 메서드 추출 => 공통 예외 처리로 일관성 높이기 위함 => entityfinder로 중앙 관리
    private Curation getCurationById(Long curationId) {
        return EntityFinder.findByIdOrThrow(curationRepository.findById(curationId)
                , ErrorCode.CURATIONS_NOT_FOUND_EXCEPTION);
    }

    private User getUserByEmail(String email) {
        return EntityFinder.findByEmailOrThrow(userRepository.findByEmail(email)
                , ErrorCode.USER_NOT_FOUND_EXCEPTION);
    }
}
