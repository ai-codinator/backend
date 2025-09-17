package com.aicodinator.backend.domain.region.service;

import com.aicodinator.backend.domain.region.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegionService {
    private final RegionRepository regionRepository;
}
