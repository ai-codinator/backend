package com.aicodinator.backend.domain.user.service;

import com.aicodinator.backend.domain.selfcheck.repository.SelfCheckRepository;
import com.aicodinator.backend.domain.user.domain.dto.UserInfoDto;
import com.aicodinator.backend.domain.user.domain.entity.User;
import com.aicodinator.backend.domain.user.repository.UserRepository;
import com.aicodinator.backend.global.exception.CustomException;
import com.aicodinator.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final AuthService authService;
    private final SelfCheckRepository selfCheckRepository;

    /**
     * 현재 로그인된 사용자의 프로필 정보 조회 (마이페이지용)
     */
    public UserInfoDto getCurrentUserProfile() {
        User user = authService.getCurrentUserEntity();
        return convertToUserInfoDto(user);
    }

    /**
     * 사용자 프로필 정보 수정
     */
    @Transactional
    public UserInfoDto updateUserProfile(UserInfoDto userInfoDto) {
        User user = authService.getCurrentUserEntity();

        // 수정 가능한 필드만 업데이트
        if (userInfoDto.getName() != null) {
            user.setName(userInfoDto.getName());
        }
        if (userInfoDto.getEmail() != null) {
            user.setEmail(userInfoDto.getEmail());
        }

        User savedUser = userRepository.save(user);
        return convertToUserInfoDto(savedUser);
    }

    /**
     * 계정 활성화 상태 토글
     */
    @Transactional
    public UserInfoDto toggleAccountStatus() {
        User user = authService.getCurrentUserEntity();
        user.setActive(!user.isActive());

        User savedUser = userRepository.save(user);
        return convertToUserInfoDto(savedUser);
    }

    /**
     * ID로 사용자 조회
     */
    public UserInfoDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return convertToUserInfoDto(user);
    }

    /**
     * User 엔티티를 UserInfoDto로 변환 (설문조사 완료 여부 포함)
     */
    private UserInfoDto convertToUserInfoDto(User user) {
        // SelfCheck 테이블에서 해당 사용자의 설문조사 데이터 존재 여부 확인
        boolean hasSurvey = selfCheckRepository.findByUser(user).isPresent();

        return UserInfoDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .socialPlatform(user.getSocialPlatform())
                .role(user.getRole())
                .active(user.isActive())
                .surveyId(user.getSurveyId())
                .hasSurvey(hasSurvey)  // 설문조사 완료 여부
                .build();
    }
}
