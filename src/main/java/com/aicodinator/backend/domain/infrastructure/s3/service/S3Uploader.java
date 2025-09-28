package com.aicodinator.backend.domain.infrastructure.s3.service;

import com.aicodinator.backend.domain.infrastructure.s3.domain.constant.FileExtension;
import com.aicodinator.backend.domain.infrastructure.s3.domain.constant.UploadType;
import com.aicodinator.backend.global.exception.CustomException;
import com.aicodinator.backend.global.exception.ErrorCode;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Uploader {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * S3 파일 업로드
     * @param file 업로드할 MultipartFile
     * @param uploadType 파일 용도 (COMMUNITY_POST / REPORT)
     * @return 업로드된 파일의 S3 키
     */
    public String upload(MultipartFile file, UploadType uploadType) {
        // 원본 파일명 추출
        String originalFileName =  file.getOriginalFilename();

        // 파일 확장자 Enum 추출
        FileExtension extension = FileExtension.fromFileName(originalFileName);

        // 저장 경로 생성
        String storedKey = generateStoredKey(uploadType, extension);
        log.debug("S3에 저장될 파일명이 생성되었습니다: {}", storedKey);

        // 메타 데이터 생성
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(extension.getContentType());

        // S3에 파일 업로드
        try {
            amazonS3.putObject(new PutObjectRequest(bucket, storedKey, file.getInputStream(), objectMetadata));
            log.debug("S3 파일 업로드에 성공했습니다: {}", storedKey);
        } catch (AmazonServiceException ase) {
            log.error("S3 파일 업로드 중 AmazonServiceException가 발생했습니다: {} {} {} ", bucket, storedKey, ase.getMessage());
            throw new CustomException(ErrorCode.S3_UPLOAD_AMAZON_ERROR, "S3 서비스 에러로 인해 파일 업로드에 실패했습니다.");
        } catch (AmazonClientException ace) {
            log.error("S3 파일 업로드 중 AmazonClientException가 발생했습니다: {} {} {} ", bucket, storedKey, ace.getMessage());
            throw new CustomException(ErrorCode.S3_UPLOAD_AMAZON_ERROR, "S3 클라이언트 에러로 인해 파일 업로드에 실패했습니다.");
        } catch (IOException e) {
            log.error("S3 파일 업로드 중 파일 스트림 오류가 발생했습니다: {} {} {}", bucket, storedKey, e.getMessage());
            throw new CustomException(ErrorCode.S3_UPLOAD_FAILED);
        }

        // 업로드한 파일의 S3 키 반환
        return storedKey;
    }

    /**
     * S3 파일 삭제
     * @param storedKey 삭제할 파일의 S3 키
     */
    public void delete(String storedKey) {
        // S3 키 검증
        if (storedKey == null || storedKey.isEmpty()) {
            log.warn("삭제할 S3 키가 비어있거나 존재하지 않습니다.");
            return;
        }

        // S3에서 파일 삭제
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(bucket, storedKey));
            log.debug("S3 파일 삭제에 성공했습니다: {}", storedKey);
        } catch (AmazonServiceException ase) {
            log.error("S3 파일 삭제 중 AmazonServiceException가 발생했습니다: {} {} {} ", bucket, storedKey, ase.getMessage());
            throw new CustomException(ErrorCode.S3_DELETE_AMAZON_ERROR, "S3 서비스 에러로 인해 파일 삭제에 실패했습니다.");
        } catch (AmazonClientException ace) {
            log.error("S3 파일 삭제 중 AmazonClientException가 발생했습니다: {} {} {} ", bucket, storedKey, ace.getMessage());
            throw new CustomException(ErrorCode.S3_DELETE_AMAZON_ERROR, "S3 클라이언트 에러로 인해 파일 삭제에 실패했습니다.");
        } catch (Exception e) {
            log.error("S3 파일 삭제 중 오류가 발생했습니다: {} {} {}", bucket, storedKey, e.getMessage());
            throw new CustomException(ErrorCode.S3_DELETE_FAILED);
        }
    }

    /**
     * dirName/yyyyMMdd/UUID.(확장자) 저장 경로 생성
     * @param uploadType UploadType
     * @return 생성된 저장 경로
     */
    private String generateStoredKey(UploadType uploadType, FileExtension extension) {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        return String.format("%s/%s/%s.%s", uploadType, date, UUID.randomUUID(), extension.getExtension());
    }
}
