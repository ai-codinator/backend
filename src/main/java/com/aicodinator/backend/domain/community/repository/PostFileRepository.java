package com.aicodinator.backend.domain.community.repository;

import com.aicodinator.backend.domain.community.domain.entity.PostFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostFileRepository extends JpaRepository<PostFile, Long> {
}
