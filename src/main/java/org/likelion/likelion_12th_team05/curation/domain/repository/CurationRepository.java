package org.likelion.likelion_12th_team05.curation.domain.repository;

import org.likelion.likelion_12th_team05.curation.domain.Curation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CurationRepository extends JpaRepository<Curation, Long> {
    List<Curation> findByNameContaining(String query);
    Page<Curation> findAll(Pageable pageable);

    Optional<Curation> findById(Long curationId);
    // 그냥 랜덤하게 5개 조회
    // 최신순 조회
    // 인기순 조회
}
