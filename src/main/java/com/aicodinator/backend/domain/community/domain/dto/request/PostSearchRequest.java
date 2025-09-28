package com.aicodinator.backend.domain.community.domain.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
@NoArgsConstructor
public class PostSearchRequest {
    private Long regionId;

    private Long boardId;

    @NotBlank
    private String keyword;

    private int page = 0;

    private int size = 10;

    public Pageable toPageable() {
        return PageRequest.of(page, size);
    }
}
