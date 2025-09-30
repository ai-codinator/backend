package com.aicodinator.backend.global.security;

import com.aicodinator.backend.domain.user.domain.constant.Role;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
public class CustomOAuth2User implements OAuth2User {

    private final Long userId;
    private final String email;
    private final String name;
    private final Role role;
    private final Map<String, Object> attributes;

    public CustomOAuth2User(Long userId, String email, String name, Role role, Map<String, Object> attributes) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.role = role;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getName() {
        return name;
    }

    public Long getUserId() {
        return userId;
    }
}
