package com.aicodinator.backend.domain.region.controller;

import com.aicodinator.backend.domain.region.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegionController {
    private final RegionService regionService;
}
