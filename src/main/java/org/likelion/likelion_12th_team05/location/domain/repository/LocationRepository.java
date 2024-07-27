package org.likelion.likelion_12th_team05.location.domain.repository;

import org.likelion.likelion_12th_team05.location.domain.Location;
import org.likelion.likelion_12th_team05.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> findByCurationId(Long curationId);
    List<Location> findByUserEmail(String email);
}
