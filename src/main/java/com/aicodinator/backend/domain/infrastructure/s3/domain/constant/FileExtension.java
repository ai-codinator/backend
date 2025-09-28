package com.aicodinator.backend.domain.infrastructure.s3.domain.constant;

import com.aicodinator.backend.global.exception.CustomException;
import com.aicodinator.backend.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.Arrays;

/**
 * 허용되는 파일 확장자 및 관련 정보를 관리하는 Enum
 */
@Getter
@RequiredArgsConstructor
public enum FileExtension {
    // 이미지 파일
    JPG("jpg", "image/jpeg"),
    JPEG("jpeg", "image/jpeg"),
    PNG("png", "image/png"),

    // 엑셀 파일
    XLSX("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    XLS("xls", "application/vnd.ms-excel"),
    CSV("csv", "text/csv"),

    // 웹 게시 파일
    PDF("pdf", "application/pdf"),
    HTML("html", "text/html"),

    // 문서 파일
    HWP("hwp", "application/vnd.hancom.hwp"), // 또는 "application/x-hwp"
    DOCX("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    DOC("doc", "application/msword"),
    TXT("txt", "text/plain"),

    // 프레젠테이션 파일
    PPTX("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"),
    PPT("ppt", "application/vnd.ms-powerpoint"),

    // 압축 파일
    ZIP("zip", "application/zip"),
    TAR("tar", "application/x-tar");

    private final String extension;
    private final String contentType;

    /**
     * 파일명으로부터 확장자 추출 후 해당하는 FileExtension Enum 반환
     * @param fileName 원본 파일명
     * @return FileExtension Enum 객체
     */
    public static FileExtension fromFileName(String fileName) {
        // 파일명에서 확장자 추출
        String extension = StringUtils.getFilenameExtension(fileName);
        if (extension == null) {
            throw new CustomException(ErrorCode.INVALID_FILE_REQUEST, "요청하신 파일의 확장자가 존재하지 않습니다.");
        }

        // 추출된 확장자와 일치하는 Enum 반환
        return Arrays.stream(values())
            .filter(fileExtension -> fileExtension.getExtension().equals(extension.toLowerCase()))
            .findFirst()
            .orElseThrow(() -> new CustomException(ErrorCode.INVALID_FILE_REQUEST, "허용하지 않는 파일 확장자입니다. :" + extension));
    }
}
