package com.aicodinator.backend.domain.common.controller;

import com.aicodinator.backend.domain.common.domain.dto.CodeDto;
import com.aicodinator.backend.domain.common.service.CodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/codes")
@RequiredArgsConstructor
@Tag(name = "Code", description = "공통 코드 관리 API")
public class CodeController {

    private final CodeService codeService;

    /**
     * 타입별 코드 목록 조회
     */
    @GetMapping("/type/{type}")
    @Operation(summary = "타입별 코드 조회", description = "지정된 타입(MBTI, OCCP, TASK)의 활성화된 코드 목록을 조회합니다.")
    public ResponseEntity<List<CodeDto>> getCodesByType(
            @Parameter(description = "코드 타입 (MBTI, OCCP, TASK)", example = "MBTI")
            @PathVariable String type) {
        List<CodeDto> codes = codeService.getCodesByType(type);
        return ResponseEntity.ok(codes);
    }

    /**
     * MBTI 코드 목록 조회
     */
    @GetMapping("/mbti")
    @Operation(summary = "MBTI 코드 조회", description = "MBTI 유형 코드 목록을 조회합니다.")
    public ResponseEntity<List<CodeDto>> getMbtiCodes() {
        List<CodeDto> codes = codeService.getMbtiCodes();
        return ResponseEntity.ok(codes);
    }

    /**
     * 직업 분류 코드 목록 조회
     */
    @GetMapping("/occp")
    @Operation(summary = "직업 분류 코드 조회", description = "직업 분류 코드 목록을 조회합니다.")
    public ResponseEntity<List<CodeDto>> getOccpCodes() {
        List<CodeDto> codes = codeService.getOccpCodes();
        return ResponseEntity.ok(codes);
    }

    /**
     * 업무 분류 코드 목록 조회
     */
    @GetMapping("/task")
    @Operation(summary = "업무 분류 코드 조회", description = "업무 분류 코드 목록을 조회합니다.")
    public ResponseEntity<List<CodeDto>> getTaskCodes() {
        List<CodeDto> codes = codeService.getTaskCodes();
        return ResponseEntity.ok(codes);
    }

    /**
     * 모든 활성화된 코드 목록 조회
     */
    @GetMapping("/all")
    @Operation(summary = "전체 코드 조회", description = "모든 타입의 활성화된 코드 목록을 조회합니다.")
    public ResponseEntity<List<CodeDto>> getAllActiveCodes() {
        List<CodeDto> codes = codeService.getAllActiveCodes();
        return ResponseEntity.ok(codes);
    }
}
