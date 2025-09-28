package com.aicodinator.backend.domain.infrastructure.s3.service;

import com.aicodinator.backend.global.exception.CustomException;
import com.aicodinator.backend.global.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Component
public class FileValidator {
    private static final int MAX_FILE_COUNT = 5;
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    /**
     * 파일 생성 시 유효성 검증 - 최대 파일 개수 초과 여부, 각 파일별 검증
     * @param files 새로 추가될 파일 리스트
     */
    public void validate(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            return;
        }

        List<MultipartFile> actualFiles = files.stream()
            .filter(file -> file != null && !file.isEmpty())
            .toList();

        if (actualFiles.size() > MAX_FILE_COUNT) {
            log.warn("파일 업로드 개수 제한을 초과했습니다. 허용: {}개, 요청: {}개", MAX_FILE_COUNT, actualFiles.size());
            throw new CustomException(ErrorCode.INVALID_FILE_REQUEST, "파일은 최대 " + MAX_FILE_COUNT + "개까지 업로드할 수 있습니다.");
        }

        for (MultipartFile file : actualFiles) {
            validateSingleFile(file);
        }
    }

    /**
     * 파일 수정 시 유효성 검증 - 최대 파일 개수 초과 여부, 각 파일별 검증
     * @param files 새로 추가될 파일 리스트
     * @param currentFileCount 수정 전 파일 개수
     * @param deleteFileCount 삭제할 파일 개수
     */
    public void validate(List<MultipartFile> files, int currentFileCount, int deleteFileCount) {
        if (files == null || files.isEmpty()) {
            return;
        }

        List<MultipartFile> actualFiles = files.stream()
            .filter(file -> file != null && !file.isEmpty())
            .toList();

        int totalCount = currentFileCount - deleteFileCount + actualFiles.size();
        if (totalCount > MAX_FILE_COUNT) {
            log.warn("파일 업로드 개수 제한을 초과했습니다. 허용: {}개, 요청: {}개", MAX_FILE_COUNT, totalCount);
            throw new CustomException(ErrorCode.INVALID_FILE_REQUEST, "파일은 최대 " + MAX_FILE_COUNT + "개까지 업로드할 수 있습니다.");
        }

        for (MultipartFile file : actualFiles) {
            validateSingleFile(file);
        }
    }

    /**
     * 단일 파일 검증 - 파일명 존재 여부, 파일 최대 크기 초과 여부 검증
     * @param file 검증할 MultipartFile
     */
    private void validateSingleFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            log.error("업로드할 파일의 원본 파일명이 비어있거나 존재하지 않습니다.");
            throw new CustomException(ErrorCode.INVALID_FILE_REQUEST);
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new CustomException(ErrorCode.INVALID_FILE_REQUEST, "파일 크기는 " + (MAX_FILE_SIZE / 1024 / 1024) + "MB를 초과할 수 없습니다.");
        }
    }
}
