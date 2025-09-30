package com.aicodinator.backend.domain.selfcheck.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveyResponseDto {

    // 개인성향
    private String hobby;                        // 취미
    private String lifeDirectionPreference;      // 삶의 방향 설정 시 선호 (도전, 안정)
    private String mbti;                         // MBTI
    private String favoriteFood;                 // 좋아하는 음식
    private String stressReliefMethod;           // 스트레스 해소 방법

    // 관련직무
    private String experienceYears;              // 경력 년차 (1년차, 1~3년차 등)
    private String currentIndustry;              // 경력(최근) 분야 (경영, 개발, 영업 등)
    private String desiredIndustry;              // 희망직군 (경영, 개발, 데이터 등)
    private String desiredJob;                   // 희망직무 (세부 직무)
    private String careerAspiration;             // 취창업 희망 (취업, 창업 등)

    // 예산
    private String housingType;                  // 거주 형태 (자가, 전세, 월세)
    private Integer annualBudget;                // 연간 예산 (만원 단위, 숫자만 입력)
}