package com.aicodinator.backend.domain.user.domain.dto;

import com.aicodinator.backend.domain.user.domain.constant.Role;
import com.aicodinator.backend.domain.user.domain.constant.SocialPlatform;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {
    private Long id;
    private String email;
    private String name;
    private SocialPlatform socialPlatform;
    private Role role;
    private boolean active;

    // 설문조사 관련 정보
    private Long surveyId;
    private boolean hasSurvey;  // 설문조사 완료 여부
}
