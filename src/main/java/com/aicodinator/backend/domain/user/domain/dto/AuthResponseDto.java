package com.aicodinator.backend.domain.user.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDto {
    private boolean authenticated;
    private UserInfoDto user;
    private String message;
    private boolean surveyCompleted; // 설문조사 완료 여부 필드 추가
}
