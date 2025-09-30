package com.aicodinator.backend.domain.user.controller;

import com.aicodinator.backend.domain.user.domain.dto.UserInfoDto;
import com.aicodinator.backend.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "사용자 관리 API")
public class UserController {

    private final UserService userService;

    /**
     * 마이페이지용 사용자 정보 조회 (상세 정보)
     */
    @GetMapping("/profile")
    @Operation(summary = "사용자 프로필 조회", description = "마이페이지에서 현재 로그인된 사용자의 상세 정보를 조회합니다.")
    public ResponseEntity<UserInfoDto> getUserProfile() {
        UserInfoDto userInfo = userService.getCurrentUserProfile();
        return ResponseEntity.ok(userInfo);
    }

    /**
     * 사용자 정보 수정
     */
    @PutMapping("/profile")
    @Operation(summary = "사용자 프로필 수정", description = "현재 로그인된 사용자의 프로필 정보를 수정합니다.")
    public ResponseEntity<UserInfoDto> updateUserProfile(@RequestBody UserInfoDto userInfoDto) {
        UserInfoDto updatedUser = userService.updateUserProfile(userInfoDto);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * 계정 활성화 상태 변경
     */
    @PatchMapping("/status")
    @Operation(summary = "계정 상태 변경", description = "현재 사용자의 계정 활성화 상태를 변경합니다.")
    public ResponseEntity<UserInfoDto> toggleAccountStatus() {
        UserInfoDto updatedUser = userService.toggleAccountStatus();
        return ResponseEntity.ok(updatedUser);
    }
}
