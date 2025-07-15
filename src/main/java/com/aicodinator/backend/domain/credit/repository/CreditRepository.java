package com.aicodinator.backend.domain.credit.repository;

import com.aicodinator.backend.domain.credit.domain.entity.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditRepository extends JpaRepository<Credit, Long> {

}
