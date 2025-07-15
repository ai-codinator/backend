package com.aicodinator.backend.domain.payment.domain.entity;

import com.aicodinator.backend.domain.payment.domain.constant.PaymentStatus;
import com.aicodinator.backend.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String paymentNo;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private LocalDateTime paymentAt;

    private LocalDateTime cancellationAt;

    private Long price;

    private String cardName;
}
