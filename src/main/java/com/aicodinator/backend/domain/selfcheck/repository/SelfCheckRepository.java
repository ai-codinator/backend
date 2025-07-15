package com.aicodinator.backend.domain.selfcheck.repository;

import com.aicodinator.backend.domain.selfcheck.domain.entity.SelfCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SelfCheckRepository extends JpaRepository<SelfCheck, Long> {

}
