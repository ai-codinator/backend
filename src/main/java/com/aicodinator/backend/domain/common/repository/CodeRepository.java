package com.aicodinator.backend.domain.common.repository;

import com.aicodinator.backend.domain.common.domain.entity.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeRepository extends JpaRepository<Code, Long> {

    /**
     * 타입별 코드 목록 조회 (활성화된 것만, 정렬순서로 정렬)
     */
    List<Code> findByTypeAndActiveTrueOrderBySortOrderAsc(String type);

    /**
     * 타입별 코드 목록 조회 (모든 상태, 정렬순서로 정렬)
     */
    List<Code> findByTypeOrderBySortOrderAsc(String type);

    /**
     * 코드와 타입으로 조회
     */
    Code findByTypeAndCode(String type, String code);
}
