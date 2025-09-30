package com.aicodinator.backend.domain.common.service;

import com.aicodinator.backend.domain.common.domain.constant.CodeType;
import com.aicodinator.backend.domain.common.domain.dto.CodeDto;
import com.aicodinator.backend.domain.common.domain.entity.Code;
import com.aicodinator.backend.domain.common.repository.CodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CodeService {

    private final CodeRepository codeRepository;

    /**
     * 타입별 코드 목록 조회 (활성화된 코드만)
     */
    public List<CodeDto> getCodesByType(String type) {
        // CodeType enum으로 유효성 검사
        validateCodeType(type);

        List<Code> codes = codeRepository.findByTypeAndActiveTrueOrderBySortOrderAsc(type);
        return codes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * MBTI 코드 목록 조회
     */
    public List<CodeDto> getMbtiCodes() {
        return getCodesByType(CodeType.MBTI.getCode());
    }

    /**
     * 직업 분류 코드 목록 조회
     */
    public List<CodeDto> getOccpCodes() {
        return getCodesByType(CodeType.OCCP.getCode());
    }

    /**
     * 업무 분류 코드 목록 조회
     */
    public List<CodeDto> getTaskCodes() {
        return getCodesByType(CodeType.TASK.getCode());
    }

    /**
     * 모든 타입의 코드 목록 조회
     */
    public List<CodeDto> getAllActiveCodes() {
        List<Code> codes = codeRepository.findAll().stream()
                .filter(Code::isActive)
                .sorted((a, b) -> {
                    int typeCompare = a.getType().compareTo(b.getType());
                    if (typeCompare != 0) return typeCompare;
                    return Integer.compare(a.getSortOrder(), b.getSortOrder());
                })
                .collect(Collectors.toList());

        return codes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * 코드 타입 유효성 검사
     */
    private void validateCodeType(String type) {
        boolean isValid = false;
        for (CodeType codeType : CodeType.values()) {
            if (codeType.getCode().equals(type)) {
                isValid = true;
                break;
            }
        }
        if (!isValid) {
            throw new IllegalArgumentException("유효하지 않은 코드 타입입니다: " + type);
        }
    }

    /**
     * Code 엔티티를 CodeDto로 변환
     */
    private CodeDto convertToDto(Code code) {
        return CodeDto.builder()
                .id(code.getId())
                .type(code.getType())
                .code(code.getCode())
                .name(code.getName())
                .description(code.getDescription())
                .active(code.isActive())
                .sortOrder(code.getSortOrder())
                .build();
    }
}
