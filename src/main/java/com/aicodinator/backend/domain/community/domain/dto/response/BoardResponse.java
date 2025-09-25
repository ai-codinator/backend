package com.aicodinator.backend.domain.community.domain.dto.response;

import com.aicodinator.backend.domain.community.domain.constant.BoardType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class BoardResponse {
    private Long boardId;

    private Long regionId;

    private String regionName;

    private BoardType boardType;
}
