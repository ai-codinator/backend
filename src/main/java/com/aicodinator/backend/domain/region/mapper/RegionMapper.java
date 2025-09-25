package com.aicodinator.backend.domain.region.mapper;

import com.aicodinator.backend.domain.region.domain.dto.RegionResponse;
import com.aicodinator.backend.domain.region.domain.entity.Region;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RegionMapper {
    @Mapping(source = "id", target = "regionId")
    @Mapping(source = "name", target = "regionName")
    RegionResponse toRegionResponse(Region region);

    List<RegionResponse> toRegionResponseList(List<Region> regions);
}
