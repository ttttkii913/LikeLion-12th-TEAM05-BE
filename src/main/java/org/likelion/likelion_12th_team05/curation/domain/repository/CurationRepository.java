package org.likelion.likelion_12th_team05.curation.domain.repository;

import org.likelion.likelion_12th_team05.curation.domain.Curation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CurationRepository extends JpaRepository<Curation, Long> {
    List<Curation> findByNameContaining(String query);
    Page<Curation> findAll(Pageable pageable);

    Optional<Curation> findById(Long curationId);

    // 좋아요 수 많은 순으로 큐레이션 6개만 조회 (limit => sql, Curation c -> Jpql 따라서 Curation c 라고 쓰면 조회 안 됨)
    @Query(value = "SELECT * FROM curation ORDER BY like_count DESC limit 6", nativeQuery = true)
    List<Curation> findTop6ByOrderByLikeCountDesc();

    // 최신순 조회
    @Query(value = "SELECT * FROM curation ORDER BY create_date DESC limit 6", nativeQuery = true)
    List<Curation> findTop6ByOrderByCreateDateDesc();
}
