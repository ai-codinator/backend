package com.aicodinator.backend.domain.user.domain.entity;

public interface OAuthUserInfo {
    String getProviderId();
    String getEmail();
    String getName();
    String getNameAttributeKey();
}
