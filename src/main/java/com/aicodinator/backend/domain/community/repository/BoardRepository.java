package com.aicodinator.backend.domain.community.repository;

import com.aicodinator.backend.domain.community.domain.entity.Board;
import com.aicodinator.backend.domain.region.domain.entity.Region;
import com.aicodinator.backend.domain.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board,Long> {
    List<Board> findByRegion(Region region);

    List<Board> findByRegionId(Long regionId);

    /**
     * 특정 사용자가 속한 지역들의 모든 게시판 조회
     * @param user
     * @return 게시판 리스트
     */
    @Query("SELECT b FROM Board b WHERE b.region IN " +
        "(SELECT ur.region FROM UserRegion ur WHERE ur.user = :user)")
    List<Board> findBoardsForUserRegions(@Param("user") User user);

    Long region(Region region);
}
