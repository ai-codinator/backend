package com.aicodinator.backend.global.security;

import com.aicodinator.backend.domain.user.domain.constant.Role;
import com.aicodinator.backend.domain.user.domain.constant.SocialPlatform;
import com.aicodinator.backend.domain.user.domain.entity.User;
import com.aicodinator.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // OAuth2 제공자별로 사용자 정보 추출
        String socialLoginId = extractSocialLoginId(oAuth2User, registrationId);
        String email = extractEmail(oAuth2User, registrationId);
        String name = extractName(oAuth2User, registrationId);
        SocialPlatform socialPlatform = getSocialPlatform(registrationId);

        // 기존 사용자 조회 또는 새 사용자 생성
        User user = userRepository.findBySocialLoginId(socialLoginId)
                .orElseGet(() -> createNewUser(socialLoginId, email, name, socialPlatform));

        return new CustomOAuth2User(user, oAuth2User.getAttributes());
    }

    private String extractSocialLoginId(OAuth2User oAuth2User, String registrationId) {
        switch (registrationId) {
            case "google":
                return oAuth2User.getAttribute("sub");
            case "kakao":
                return String.valueOf(oAuth2User.getAttribute("id"));
            case "naver":
                return ((Map<String, Object>) oAuth2User.getAttribute("response")).get("id").toString();
            default:
                throw new IllegalArgumentException("지원하지 않는 소셜 로그인 제공자입니다: " + registrationId);
        }
    }

    private String extractEmail(OAuth2User oAuth2User, String registrationId) {
        switch (registrationId) {
            case "google":
                return oAuth2User.getAttribute("email");
            case "kakao":
                Map<String, Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");
                return kakaoAccount != null ? (String) kakaoAccount.get("email") : null;
            case "naver":
                return ((Map<String, Object>) oAuth2User.getAttribute("response")).get("email").toString();
            default:
                return null;
        }
    }

    private String extractName(OAuth2User oAuth2User, String registrationId) {
        switch (registrationId) {
            case "google":
                return oAuth2User.getAttribute("name");
            case "kakao":
                Map<String, Object> properties = oAuth2User.getAttribute("properties");
                return properties != null ? (String) properties.get("nickname") : null;
            case "naver":
                return ((Map<String, Object>) oAuth2User.getAttribute("response")).get("name").toString();
            default:
                return null;
        }
    }

    private SocialPlatform getSocialPlatform(String registrationId) {
        switch (registrationId) {
            case "google":
                return SocialPlatform.GOOGLE;
            case "kakao":
                return SocialPlatform.KAKAO;
            case "naver":
                return SocialPlatform.NAVER;
            default:
                throw new IllegalArgumentException("지원하지 않는 소셜 로그인 제공자입니다: " + registrationId);
        }
    }

    private User createNewUser(String socialLoginId, String email, String name, SocialPlatform socialPlatform) {
        User newUser = User.builder()
                .socialLoginId(socialLoginId)
                .email(email)
                .name(name)
                .socialPlatform(socialPlatform)
                .role(Role.USER)
                .active(true)
                .build();

        return userRepository.save(newUser);
    }
}
