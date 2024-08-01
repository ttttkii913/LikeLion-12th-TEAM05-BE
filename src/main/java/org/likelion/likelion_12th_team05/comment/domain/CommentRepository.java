package org.likelion.likelion_12th_team05.comment.domain;

import org.likelion.likelion_12th_team05.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    boolean existsById (User user);
}
