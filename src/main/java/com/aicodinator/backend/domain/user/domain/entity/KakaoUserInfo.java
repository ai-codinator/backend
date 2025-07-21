package com.aicodinator.backend.domain.user.domain.entity;

import java.util.Map;

public class KakaoUserInfo implements OAuthUserInfo {
    private final Map<String, Object> attrs;

    public KakaoUserInfo(Map<String, Object> attrs) {
        this.attrs = attrs;
    }

    @Override
    public String getProviderId() {
        return attrs.get("id").toString();
    }

    @Override
    public String getEmail() {
        @SuppressWarnings("unchecked")
        Map<String, Object> kakaoAccount = (Map<String, Object>) attrs.get("kakao_account");
        return kakaoAccount.get("email").toString();
    }

    @Override
    public String getName() {
        @SuppressWarnings("unchecked")
        Map<String, Object> properties = (Map<String, Object>) attrs.get("properties");
        return properties.get("nickname").toString();
    }

    @Override
    public String getNameAttributeKey() {
        return "id";
    }
}

