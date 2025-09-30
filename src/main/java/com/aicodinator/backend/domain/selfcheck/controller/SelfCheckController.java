package com.aicodinator.backend.domain.selfcheck.controller;

import com.aicodinator.backend.domain.selfcheck.domain.entity.SurveyResponseDto;
import com.aicodinator.backend.domain.selfcheck.service.SelfCheckService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/self-check")
@RequiredArgsConstructor
@Tag(name = "SelfCheck", description = "셀프 체크 설문 API")
public class SelfCheckController {

    private final SelfCheckService selfCheckService;

    /**
     * 현재 로그인한 사용자의 셀프 체크 설문 결과 저장
     */
    @PostMapping
    @Operation(summary = "셀프 체크 설문 결과 저장", description = "현재 로그인한 사용자의 셀프 체크 설문 결과를 저장합니다.")
    public ResponseEntity<SurveyResponseDto> createSelfCheck(@RequestBody SurveyResponseDto surveyResponseDto) {
        SurveyResponseDto result = selfCheckService.createSelfCheck(surveyResponseDto);
        return ResponseEntity.ok(result);
    }

    /**
     * 현재 로그인한 사용자의 셀프 체크 설문 결과 수정
     */
    @PutMapping
    @Operation(summary = "셀프 체크 설문 결과 수정", description = "현재 로그인한 사용자의 셀프 체크 설문 결과를 수정합니다.")
    public ResponseEntity<SurveyResponseDto> updateSelfCheck(@RequestBody SurveyResponseDto surveyResponseDto) {
        SurveyResponseDto result = selfCheckService.updateSelfCheck(surveyResponseDto);
        return ResponseEntity.ok(result);
    }

    /**
     * 현재 로그인한 사용자의 셀프 체크 설문 결과 조회
     */
    @GetMapping
    @Operation(summary = "셀프 체크 설문 결과 조회", description = "현재 로그인한 사용자의 셀프 체크 설문 결과를 조회합니다.")
    public ResponseEntity<SurveyResponseDto> getSelfCheck() {
        SurveyResponseDto result = selfCheckService.getSelfCheck();
        return ResponseEntity.ok(result);
    }

    /**
     * 현재 로그인한 사용자의 셀프 체크 설문 결과 삭제
     */
    @DeleteMapping
    @Operation(summary = "셀프 체크 설문 결과 삭제", description = "현재 로그인한 사용자의 셀프 체크 설문 결과를 삭제합니다.")
    public ResponseEntity<Void> deleteSelfCheck() {
        selfCheckService.deleteSelfCheck();
        return ResponseEntity.noContent().build();
    }
}
