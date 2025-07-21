package com.aicodinator.backend.domain.user.repository;

import com.aicodinator.backend.domain.user.domain.constant.SocialPlatform;
import com.aicodinator.backend.domain.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySocialPlatformAndSocialLoginId(
            SocialPlatform socialPlatform,
            String socialLoginId
    );
}
