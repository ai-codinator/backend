package com.aicodinator.backend.domain.user.repository;

import com.aicodinator.backend.domain.user.domain.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    
    // 사용자 ID로 프로필 조회
    Optional<UserProfile> findByUser_Id(Long userId);
    
    // 사용자 ID로 프로필 존재 여부 확인
    boolean existsByUser_Id(Long userId);
    
    // 사용자 ID로 프로필 삭제
    void deleteByUser_Id(Long userId);
}