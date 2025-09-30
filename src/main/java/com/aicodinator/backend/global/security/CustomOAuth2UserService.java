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

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 플랫폼별로 사용자 정보 추출
        UserInfo userInfo = extractUserInfo(registrationId, attributes);

        // 사용자 저장 또는 업데이트
        User user = saveOrUpdateUser(userInfo);

        return new CustomOAuth2User(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole(),
                attributes
        );
    }

    private UserInfo extractUserInfo(String registrationId, Map<String, Object> attributes) {
        switch (registrationId) {
            case "google":
                return UserInfo.builder()
                        .socialLoginId((String) attributes.get("sub"))
                        .email((String) attributes.get("email"))
                        .name((String) attributes.get("name"))
                        .socialPlatform(SocialPlatform.GOOGLE)
                        .build();

            case "naver":
                Map<String, Object> response = (Map<String, Object>) attributes.get("response");
                return UserInfo.builder()
                        .socialLoginId((String) response.get("id"))
                        .email((String) response.get("email"))
                        .name((String) response.get("name"))
                        .socialPlatform(SocialPlatform.NAVER)
                        .build();

            case "kakao":
                Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
                Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
                return UserInfo.builder()
                        .socialLoginId(String.valueOf(attributes.get("id")))
                        .email((String) kakaoAccount.get("email"))
                        .name((String) profile.get("nickname"))
                        .socialPlatform(SocialPlatform.KAKAO)
                        .build();

            default:
                throw new OAuth2AuthenticationException("Unsupported provider: " + registrationId);
        }
    }

    private User saveOrUpdateUser(UserInfo userInfo) {
        return userRepository.findBySocialLoginIdAndSocialPlatform(
                userInfo.getSocialLoginId(),
                userInfo.getSocialPlatform()
        )
        .map(existingUser -> {
            // 기존 사용자 정보 업데이트
            existingUser.setEmail(userInfo.getEmail());
            existingUser.setName(userInfo.getName());
            return userRepository.save(existingUser);
        })
        .orElseGet(() -> {
            // 새 사용자 생성
            User newUser = User.builder()
                    .socialLoginId(userInfo.getSocialLoginId())
                    .email(userInfo.getEmail())
                    .name(userInfo.getName())
                    .socialPlatform(userInfo.getSocialPlatform())
                    .role(Role.USER)
                    .active(true)
                    .build();
            return userRepository.save(newUser);
        });
    }

    // 내부 클래스로 사용자 정보 DTO
    private static class UserInfo {
        private final String socialLoginId;
        private final String email;
        private final String name;
        private final SocialPlatform socialPlatform;

        private UserInfo(String socialLoginId, String email, String name, SocialPlatform socialPlatform) {
            this.socialLoginId = socialLoginId;
            this.email = email;
            this.name = name;
            this.socialPlatform = socialPlatform;
        }

        public static UserInfoBuilder builder() {
            return new UserInfoBuilder();
        }

        public String getSocialLoginId() { return socialLoginId; }
        public String getEmail() { return email; }
        public String getName() { return name; }
        public SocialPlatform getSocialPlatform() { return socialPlatform; }

        public static class UserInfoBuilder {
            private String socialLoginId;
            private String email;
            private String name;
            private SocialPlatform socialPlatform;

            public UserInfoBuilder socialLoginId(String socialLoginId) {
                this.socialLoginId = socialLoginId;
                return this;
            }

            public UserInfoBuilder email(String email) {
                this.email = email;
                return this;
            }

            public UserInfoBuilder name(String name) {
                this.name = name;
                return this;
            }

            public UserInfoBuilder socialPlatform(SocialPlatform socialPlatform) {
                this.socialPlatform = socialPlatform;
                return this;
            }

            public UserInfo build() {
                return new UserInfo(socialLoginId, email, name, socialPlatform);
            }
        }
    }
}
