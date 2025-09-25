package com.aicodinator.backend.domain.community.mapper;

import com.aicodinator.backend.domain.community.domain.dto.response.BoardResponse;
import com.aicodinator.backend.domain.community.domain.entity.Board;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BoardMapper {
    @Mapping(source = "id", target = "boardId")
    @Mapping(source = "region.id", target = "regionId")
    @Mapping(source = "region.name", target = "regionName")
    BoardResponse toBoardResponse(Board board);

    List<BoardResponse> toBoardResponseList(List<Board> boards);
}
