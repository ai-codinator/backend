package com.aicodinator.backend.domain.region.service;

import com.aicodinator.backend.domain.region.domain.Region;
import com.aicodinator.backend.domain.region.repository.RegionRepository;
import com.aicodinator.backend.domain.user.domain.entity.User;
import com.aicodinator.backend.global.exception.CustomException;
import com.aicodinator.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegionService {
    private final RegionRepository regionRepository;

    public Region findById(Long regionId) {
        return regionRepository.findById(regionId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "요청하신 지역을 찾을 수 없습니다."));
    }

    public List<Region> findMyRegions(User user) {
        return regionRepository.findRegionsByUser(user);
    }
}
