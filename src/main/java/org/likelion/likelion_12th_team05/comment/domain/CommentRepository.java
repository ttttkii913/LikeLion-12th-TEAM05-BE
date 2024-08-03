package org.likelion.likelion_12th_team05.comment.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findById(Long curationId);
    List<Comment> findAllByCurationId(Long curationId);
}
