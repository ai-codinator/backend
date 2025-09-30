package com.aicodinator.backend.domain.user.service;

import com.aicodinator.backend.domain.selfcheck.repository.SelfCheckRepository;
import com.aicodinator.backend.domain.user.domain.dto.AuthResponseDto;
import com.aicodinator.backend.domain.user.domain.dto.UserInfoDto;
import com.aicodinator.backend.domain.user.domain.entity.User;
import com.aicodinator.backend.domain.user.repository.UserRepository;
import com.aicodinator.backend.global.exception.CustomException;
import com.aicodinator.backend.global.exception.ErrorCode;
import com.aicodinator.backend.global.security.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final SelfCheckRepository selfCheckRepository;

    /**
     * 현재 인증된 사용자 정보 조회
     */
    public AuthResponseDto getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
            "anonymousUser".equals(authentication.getPrincipal())) {
            return AuthResponseDto.builder()
                    .authenticated(false)
                    .message("인증되지 않은 사용자입니다.")
                    .surveyCompleted(false)
                    .build();
        }

        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            Long userId = oAuth2User.getUserId();

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

            // 설문조사 완료 여부 확인
            boolean hasSurvey = selfCheckRepository.findByUser(user).isPresent();

            UserInfoDto userInfoDto = UserInfoDto.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .name(user.getName())
                    .socialPlatform(user.getSocialPlatform())
                    .role(user.getRole())
                    .active(user.isActive())
                    .surveyId(user.getSurveyId())
                    .hasSurvey(hasSurvey)
                    .build();

            return AuthResponseDto.builder()
                    .authenticated(true)
                    .user(userInfoDto)
                    .message("인증된 사용자입니다.")
                    .surveyCompleted(hasSurvey)
                    .build();

        } catch (Exception e) {
            return AuthResponseDto.builder()
                    .authenticated(false)
                    .message("사용자 정보를 가져올 수 없습니다.")
                    .surveyCompleted(false)
                    .build();
        }
    }

    /**
     * 세션 유효성 검증
     */
    public boolean isSessionValid() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null &&
               authentication.isAuthenticated() &&
               !"anonymousUser".equals(authentication.getPrincipal());
    }

    /**
     * 현재 로그인된 사용자 ID 가져오기
     */
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
            "anonymousUser".equals(authentication.getPrincipal())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        return oAuth2User.getUserId();
    }

    /**
     * 현재 로그인된 사용자 엔티티 가져오기
     */
    public User getCurrentUserEntity() {
        Long userId = getCurrentUserId();
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
