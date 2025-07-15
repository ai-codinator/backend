package com.aicodinator.backend.domain.credit.domain.entity;

import com.aicodinator.backend.domain.credit.domain.constant.CreditStatus;
import com.aicodinator.backend.domain.payment.domain.entity.Payment;
import com.aicodinator.backend.domain.user.domain.entity.User;
import com.aicodinator.backend.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
public class Credit extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "payment_id", unique = true)
    private Payment payment;

    private String creditTrade;

    private String creditNo;

    @Enumerated(EnumType.STRING)
    private CreditStatus status;
}
