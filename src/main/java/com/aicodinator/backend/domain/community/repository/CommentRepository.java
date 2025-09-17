package com.aicodinator.backend.domain.community.repository;

import com.aicodinator.backend.domain.community.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
