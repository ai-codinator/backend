package com.aicodinator.backend.domain.region.repository;

import com.aicodinator.backend.domain.region.domain.entity.Region;
import com.aicodinator.backend.domain.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionRepository extends JpaRepository<Region,Long> {
    /**
     * 특정 사용자의 지역 조회
     * @param user 조회할 사용자
     * @return 해당 사용자의 지역 리스트
     */
    @Query("SELECT ur.region FROM UserRegion ur WHERE ur.user = :user")
    List<Region> findRegionsByUser(@Param("user") User user);
}
