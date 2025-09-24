package com.aicodinator.backend.domain.community.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PostRequest {
    @NotNull(message = "게시판 Id는 필수입니다.")
    private Long boardId;

    @NotBlank(message = "게시글 제목은 필수입니다.")
    @Size(max = 255, message = "제목은 255자 이내로 입력해주세요.")
    private String title;

    @Size(max = 1000, message = "게시글 내용은 1000자 이내로 작성해주세요.")
    private String content;
}
