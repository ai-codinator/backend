package com.aicodinator.backend.domain.selfcheck.service;

import com.aicodinator.backend.domain.selfcheck.domain.entity.SelfCheck;
import com.aicodinator.backend.domain.selfcheck.domain.entity.SurveyResponseDto;
import com.aicodinator.backend.domain.selfcheck.repository.SelfCheckRepository;
import com.aicodinator.backend.domain.user.domain.entity.User;
import com.aicodinator.backend.domain.user.service.AuthService;
import com.aicodinator.backend.global.exception.CustomException;
import com.aicodinator.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SelfCheckService {

    private final SelfCheckRepository selfCheckRepository;
    private final AuthService authService;

    /**
     * 현재 로그인한 사용자의 셀프 체크 설문 결과 저장
     */
    @Transactional
    public SurveyResponseDto createSelfCheck(SurveyResponseDto surveyResponseDto) {
        User user = authService.getCurrentUserEntity();

        // 이미 설문 결과가 있는지 확인
        if (selfCheckRepository.findByUser(user).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_RESOURCE);
        }

        SelfCheck selfCheck = SelfCheck.builder()
                .user(user)
                .hobby(surveyResponseDto.getHobby())
                .lifeDirectionPreference(surveyResponseDto.getLifeDirectionPreference())
                .mbti(surveyResponseDto.getMbti())
                .favoriteFood(surveyResponseDto.getFavoriteFood())
                .stressReliefMethod(surveyResponseDto.getStressReliefMethod())
                .experienceYears(surveyResponseDto.getExperienceYears())
                .currentIndustry(surveyResponseDto.getCurrentIndustry())
                .desiredIndustry(surveyResponseDto.getDesiredIndustry())
                .desiredJob(surveyResponseDto.getDesiredJob())
                .careerAspiration(surveyResponseDto.getCareerAspiration())
                .housingType(surveyResponseDto.getHousingType())
                .annualBudget(surveyResponseDto.getAnnualBudget())
                .build();

        SelfCheck savedSelfCheck = selfCheckRepository.save(selfCheck);
        return convertToDto(savedSelfCheck);
    }

    /**
     * 현재 로그인한 사용자의 셀프 체크 설문 결과 수정
     */
    @Transactional
    public SurveyResponseDto updateSelfCheck(SurveyResponseDto surveyResponseDto) {
        User user = authService.getCurrentUserEntity();

        SelfCheck existingSelfCheck = selfCheckRepository.findByUser(user)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        // 필드 업데이트
        existingSelfCheck.setHobby(surveyResponseDto.getHobby());
        existingSelfCheck.setLifeDirectionPreference(surveyResponseDto.getLifeDirectionPreference());
        existingSelfCheck.setMbti(surveyResponseDto.getMbti());
        existingSelfCheck.setFavoriteFood(surveyResponseDto.getFavoriteFood());
        existingSelfCheck.setStressReliefMethod(surveyResponseDto.getStressReliefMethod());
        existingSelfCheck.setExperienceYears(surveyResponseDto.getExperienceYears());
        existingSelfCheck.setCurrentIndustry(surveyResponseDto.getCurrentIndustry());
        existingSelfCheck.setDesiredIndustry(surveyResponseDto.getDesiredIndustry());
        existingSelfCheck.setDesiredJob(surveyResponseDto.getDesiredJob());
        existingSelfCheck.setCareerAspiration(surveyResponseDto.getCareerAspiration());
        existingSelfCheck.setHousingType(surveyResponseDto.getHousingType());
        existingSelfCheck.setAnnualBudget(surveyResponseDto.getAnnualBudget());

        SelfCheck updatedSelfCheck = selfCheckRepository.save(existingSelfCheck);
        return convertToDto(updatedSelfCheck);
    }

    /**
     * 현재 로그인한 사용자의 셀프 체크 설문 결과 조회
     */
    public SurveyResponseDto getSelfCheck() {
        User user = authService.getCurrentUserEntity();

        SelfCheck selfCheck = selfCheckRepository.findByUser(user)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        return convertToDto(selfCheck);
    }

    /**
     * 현재 로그인한 사용자의 셀프 체크 설문 결과 삭제
     */
    @Transactional
    public void deleteSelfCheck() {
        User user = authService.getCurrentUserEntity();

        SelfCheck selfCheck = selfCheckRepository.findByUser(user)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        selfCheckRepository.delete(selfCheck);
    }

    /**
     * SelfCheck 엔티티를 DTO로 변환
     */
    private SurveyResponseDto convertToDto(SelfCheck selfCheck) {
        return SurveyResponseDto.builder()
                .hobby(selfCheck.getHobby())
                .lifeDirectionPreference(selfCheck.getLifeDirectionPreference())
                .mbti(selfCheck.getMbti())
                .favoriteFood(selfCheck.getFavoriteFood())
                .stressReliefMethod(selfCheck.getStressReliefMethod())
                .experienceYears(selfCheck.getExperienceYears())
                .currentIndustry(selfCheck.getCurrentIndustry())
                .desiredIndustry(selfCheck.getDesiredIndustry())
                .desiredJob(selfCheck.getDesiredJob())
                .careerAspiration(selfCheck.getCareerAspiration())
                .housingType(selfCheck.getHousingType())
                .annualBudget(selfCheck.getAnnualBudget())
                .build();
    }
}
