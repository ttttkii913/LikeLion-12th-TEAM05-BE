package org.likelion.likelion_12th_team05.user.domain.repository;

import org.likelion.likelion_12th_team05.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    Optional<User> findById(Long id);

    // 랜딩 페이지 - curation 가장 많이 작성한 사용자 6명씩 조회
    Page<User> findAllByOrderByCurationCountDesc(Pageable pageable);
}
