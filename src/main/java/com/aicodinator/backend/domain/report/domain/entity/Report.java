package com.aicodinator.backend.domain.report.domain.entity;

import com.aicodinator.backend.domain.selfcheck.domain.entity.SelfCheck;
import com.aicodinator.backend.domain.user.domain.entity.User;
import com.aicodinator.backend.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Report extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "self_check_id", unique = true)
    private SelfCheck selfCheck;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    private String recommendedLocation;

    private String housingInfo;

    private String aiStory;

    private String recommendedJob;

    private String recommendedPolicy;
}
