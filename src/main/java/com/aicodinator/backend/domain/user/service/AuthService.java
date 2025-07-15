package com.aicodinator.backend.domain.user.service;

import com.aicodinator.backend.domain.user.repository.RefreshTokenRepository;
import com.aicodinator.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
}
