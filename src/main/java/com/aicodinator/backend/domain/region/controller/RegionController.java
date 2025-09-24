package com.aicodinator.backend.domain.region.controller;

import com.aicodinator.backend.domain.region.domain.Region;
import com.aicodinator.backend.domain.region.service.RegionService;
import com.aicodinator.backend.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/regions")
public class RegionController {
    private final RegionService regionService;

    @GetMapping("/my")
    public ResponseEntity<List<Region>> findMyRegions(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(regionService.findMyRegions(user));
    }
    // TODO: CustomOAuth2UserPrincipal로 변경
}
