package com.aicodinator.backend.domain.user.controller;

import com.aicodinator.backend.domain.user.domain.dto.AuthResponseDto;
import com.aicodinator.backend.domain.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "인증 관련 API")
public class AuthController {

    private final AuthService authService;

    /**
     * 마이페이지용 현재 사용자 정보 조회
     */
    @GetMapping("/me")
    @Operation(summary = "현재 사용자 정보 조회", description = "마이페이지에서 현재 로그인된 사용자 정보를 조회합니다.")
    public ResponseEntity<AuthResponseDto> getCurrentUser() {
        AuthResponseDto authResponse = authService.getCurrentUser();
        return ResponseEntity.ok(authResponse);
    }

    /**
     * 세션 유효성 검증
     */
    @GetMapping("/check")
    @Operation(summary = "세션 유효성 검증", description = "현재 세션이 유효한지 확인합니다.")
    public ResponseEntity<AuthResponseDto> checkSession() {
        boolean isValid = authService.isSessionValid();

        if (isValid) {
            return ResponseEntity.ok(AuthResponseDto.builder()
                    .authenticated(true)
                    .message("유효한 세션입니다.")
                    .build());
        } else {
            return ResponseEntity.ok(AuthResponseDto.builder()
                    .authenticated(false)
                    .message("유효하지 않은 세션입니다.")
                    .build());
        }
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "현재 세션을 무효화하고 로그아웃합니다.")
    public ResponseEntity<AuthResponseDto> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity.ok(AuthResponseDto.builder()
                .authenticated(false)
                .message("로그아웃되었습니다.")
                .build());
    }

    /**
     * OAuth2 로그인 성공 핸들러
     */
    @GetMapping("/success")
    @Operation(summary = "OAuth2 로그인 성공", description = "OAuth2 로그인 성공 후 리다이렉션되는 엔드포인트입니다.")
    public ResponseEntity<AuthResponseDto> loginSuccess() {
        AuthResponseDto authResponse = authService.getCurrentUser();
        return ResponseEntity.ok(authResponse);
    }

    /**
     * OAuth2 로그인 실패 핸들러
     */
    @GetMapping("/failure")
    @Operation(summary = "OAuth2 로그인 실패", description = "OAuth2 로그인 실패 시 리다이렉션되는 엔드포인트입니다.")
    public ResponseEntity<AuthResponseDto> loginFailure() {
        return ResponseEntity.badRequest()
                .body(AuthResponseDto.builder()
                        .authenticated(false)
                        .message("로그인에 실패했습니다.")
                        .build());
    }

    /**
     * OAuth2 로그인 URL 제공
     */
    @GetMapping("/oauth2/urls")
    @Operation(summary = "OAuth2 로그인 URL 조회", description = "소셜 로그인을 위한 OAuth2 URL들을 제공합니다.")
    public ResponseEntity<?> getOAuth2Urls(HttpServletRequest request) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();

        return ResponseEntity.ok(new Object() {
            public final String google = baseUrl + "/oauth2/authorization/google";
            public final String naver = baseUrl + "/oauth2/authorization/naver";
            public final String kakao = baseUrl + "/oauth2/authorization/kakao";
        });
    }
}
