package com.aicodinator.backend.domain.user.domain.entity;

import com.aicodinator.backend.domain.user.domain.constant.Role;
import com.aicodinator.backend.domain.user.domain.constant.SocialPlatform;
import com.aicodinator.backend.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String socialLoginId;

    private String email;

    private String name;

    @Enumerated(EnumType.STRING)
    private SocialPlatform socialPlatform;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean active;
}
