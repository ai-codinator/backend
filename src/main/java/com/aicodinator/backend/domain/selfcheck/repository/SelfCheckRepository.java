package com.aicodinator.backend.domain.selfcheck.repository;

import com.aicodinator.backend.domain.selfcheck.domain.entity.SelfCheck;
import com.aicodinator.backend.domain.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SelfCheckRepository extends JpaRepository<SelfCheck, Long> {
    Optional<SelfCheck> findByUser(User user);
    Optional<SelfCheck> findByUserId(Long userId);
}
