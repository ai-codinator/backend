package com.aicodinator.backend.domain.common.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeDto {
    private Long id;
    private String type;
    private String code;
    private String name;
    private String description;
    private boolean active;
    private int sortOrder;
}
