package com.aicodinator.backend.domain.community.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PostFileResponse {
    private Long fileId;

    private String originalFileName;

    private String fileUrl;
}
