package com.aicodinator.backend.domain.community.mapper;

import com.aicodinator.backend.domain.community.domain.dto.response.PostFileResponse;
import com.aicodinator.backend.domain.community.domain.entity.PostFile;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Slf4j
@Mapper(componentModel = "spring")
public abstract class PostFileMapper {
    protected String s3BaseUrl;

    @Autowired
    public void setS3BaseUrl(@Value("${cloud.aws.s3.base-url}") String s3BaseUrl) {
        log.info("S3 Base URL Injected: {}", s3BaseUrl);
        this.s3BaseUrl = s3BaseUrl;
    }

    @Mapping(source = "id", target = "fileId")
    public abstract PostFileResponse toPostFileResponse(PostFile postFile);

    @AfterMapping
    protected void afterMapping(PostFile postFile, @MappingTarget PostFileResponse.PostFileResponseBuilder responseBuilder) {
        log.info("s3BaseUrl in afterMapping: {}", s3BaseUrl);
        log.info("StoredKey in afterMapping: {}", postFile.getStoredKey());
        responseBuilder.fileUrl(s3BaseUrl + postFile.getStoredKey());
    }
}
