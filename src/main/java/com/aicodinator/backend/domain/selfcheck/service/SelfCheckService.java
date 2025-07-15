package com.aicodinator.backend.domain.selfcheck.service;

import com.aicodinator.backend.domain.selfcheck.repository.SelfCheckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SelfCheckService {
    private final SelfCheckRepository selfCheckRepository;
}
