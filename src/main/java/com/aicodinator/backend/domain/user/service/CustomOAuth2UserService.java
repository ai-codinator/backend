package com.aicodinator.backend.domain.user.service;

import com.aicodinator.backend.domain.user.domain.constant.Role;
import com.aicodinator.backend.domain.user.domain.constant.SocialPlatform;
import com.aicodinator.backend.domain.user.domain.entity.*;
import com.aicodinator.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService
        implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {

        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest
                .getClientRegistration()
                .getRegistrationId();    // "google", "naver", or "kakao"
        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuthUserInfo userInfo;
        SocialPlatform platform;

        if ("google".equals(registrationId)) {
            userInfo = new GoogleUserInfo(attributes);
            platform = SocialPlatform.GOOGLE;

        } else if ("naver".equals(registrationId)) {
            @SuppressWarnings("unchecked")
            Map<String, Object> resp = (Map<String, Object>) attributes.get("response");
            userInfo = new NaverUserInfo(resp);
            platform = SocialPlatform.NAVER;

        } else if ("kakao".equals(registrationId)) {
            userInfo = new KakaoUserInfo(attributes);
            platform = SocialPlatform.KAKAO;

        } else {
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("invalid_registration"),
                    "지원하지 않는 소셜 플랫폼: " + registrationId
            );
        }

        // DB 조회 또는 신규 생성
        User user = userRepository
                .findBySocialPlatformAndSocialLoginId(platform, userInfo.getProviderId())
                .orElseGet(() -> {
                    User u = User.builder()
                            .socialPlatform(platform)
                            .socialLoginId(userInfo.getProviderId())
                            .email(userInfo.getEmail())
                            .name(userInfo.getName())
                            .role(Role.USER)
                            .active(true)
                            .build();
                    return userRepository.save(u);
                });

        // 세션에 저장될 DefaultOAuth2User 반환
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole())),
                attributes,
                userInfo.getNameAttributeKey()
        );
    }
}