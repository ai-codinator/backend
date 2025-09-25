package com.aicodinator.backend.domain.community.repository;

import com.aicodinator.backend.domain.community.domain.entity.PostLike;
import com.aicodinator.backend.domain.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    boolean existsByPostIdAndUser(Long postId, User user);

    void deleteByPostIdAndUser(Long postId, User user);
}
