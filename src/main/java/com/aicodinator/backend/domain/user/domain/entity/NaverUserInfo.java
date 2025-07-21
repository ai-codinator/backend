package com.aicodinator.backend.domain.user.domain.entity;

import java.util.Map;

public class NaverUserInfo implements OAuthUserInfo {
    private final Map<String, Object> resp;
    public NaverUserInfo(Map<String, Object> resp) {
        this.resp = resp;
    }
    @Override public String getProviderId() { return resp.get("id").toString(); }
    @Override public String getEmail()      { return resp.get("email").toString(); }
    @Override public String getName()       { return resp.get("name").toString(); }
    @Override public String getNameAttributeKey() { return "id"; }
}
