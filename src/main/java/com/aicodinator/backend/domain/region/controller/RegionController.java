package com.aicodinator.backend.domain.region.controller;

import com.aicodinator.backend.domain.region.domain.dto.RegionResponse;
import com.aicodinator.backend.domain.region.service.RegionService;
import com.aicodinator.backend.global.security.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class RegionController {
    private final RegionService regionService;

    @GetMapping("/my/regions")
    public ResponseEntity<List<RegionResponse>> findMyRegions(@AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        return ResponseEntity.ok(regionService.findMyRegions(oAuth2User.getUser()));
    }
}
