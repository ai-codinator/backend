package com.aicodinator.backend.domain.credit.service;

import com.aicodinator.backend.domain.credit.repository.CreditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreditService {
    private final CreditRepository creditRepository;
}
