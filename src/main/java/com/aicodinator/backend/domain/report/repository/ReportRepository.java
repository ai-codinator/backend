package com.aicodinator.backend.domain.report.repository;

import com.aicodinator.backend.domain.report.domain.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

}
