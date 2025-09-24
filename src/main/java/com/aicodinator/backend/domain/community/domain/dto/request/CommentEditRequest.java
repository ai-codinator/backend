package com.aicodinator.backend.domain.community.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentEditRequest {
    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String content;
}
