package com.aicodinator.backend.domain.credit.controller;

import com.aicodinator.backend.domain.credit.service.CreditService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CreditController {
    private final CreditService creditService;
}
