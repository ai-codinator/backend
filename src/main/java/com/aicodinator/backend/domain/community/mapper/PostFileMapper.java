package com.aicodinator.backend.domain.community.mapper;

import com.aicodinator.backend.domain.community.domain.dto.response.PostFileResponse;
import com.aicodinator.backend.domain.community.domain.entity.PostFile;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Value;

@Mapper(componentModel = "spring")
public abstract class PostFileMapper {
    @Value("${cloud.aws.s3.base-url}")
    private String s3BaseUrl;

    @Mapping(source = "id", target = "fileId")
    public abstract PostFileResponse toPostFileResponse(PostFile postFile);

    @AfterMapping
    protected void afterMapping(PostFile postFile, @MappingTarget PostFileResponse response) {
        response.setFileUrl(s3BaseUrl + postFile.getStoredKey());
    }
}
