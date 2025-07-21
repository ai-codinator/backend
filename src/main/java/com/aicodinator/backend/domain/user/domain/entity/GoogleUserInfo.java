package com.aicodinator.backend.domain.user.domain.entity;

import java.util.Map;

public class GoogleUserInfo implements OAuthUserInfo {
    private final Map<String, Object> attrs;
    public GoogleUserInfo(Map<String, Object> attrs) {
        this.attrs = attrs;
    }
    @Override public String getProviderId() { return attrs.get("sub").toString(); }
    @Override public String getEmail()      { return attrs.get("email").toString(); }
    @Override public String getName()       { return attrs.get("name").toString(); }
    @Override public String getNameAttributeKey() { return "sub"; }
}
