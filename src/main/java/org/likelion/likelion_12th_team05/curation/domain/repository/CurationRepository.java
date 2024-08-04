package org.likelion.likelion_12th_team05.curation.domain.repository;

import org.likelion.likelion_12th_team05.curation.domain.Curation;
import org.likelion.likelion_12th_team05.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CurationRepository extends JpaRepository<Curation, Long> {
    Page<Curation> findByNameContaining(String query, Pageable pageable);
    Page<Curation> findAll(Pageable pageable);

    Optional<Curation> findById(Long curationId);

    // 좋아요 수 많은 순으로 큐레이션 조회 - 페이지네이션으로 6개씩 조회
    Page<Curation> findAllByOrderByLikeCountDesc(Pageable pageable);

    // 최신순 조회 - 페이지네이션으로 6개씩 조회
    Page<Curation> findAllByOrderByCreateDateDesc(Pageable pageable);

    // 사용자가 좋아요를 누른 큐레이션 목록(6개씩 페이지네이션) 조회
    @Query(value = "SELECT c FROM Curation c JOIN likes l ON c.id = l.curation.id WHERE l.user = :user")
    Page<Curation> findUserLikes(User user, Pageable pageable);

    // 댓글이 많이 달린 6개의 큐레이션
    Page<Curation> findAllByOrderByCommentCountDesc(Pageable pageable);
}
