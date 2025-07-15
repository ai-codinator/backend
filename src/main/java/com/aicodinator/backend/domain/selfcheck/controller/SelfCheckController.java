package com.aicodinator.backend.domain.selfcheck.controller;

import com.aicodinator.backend.domain.selfcheck.service.SelfCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SelfCheckController {
    private final SelfCheckService selfCheckService;
}
