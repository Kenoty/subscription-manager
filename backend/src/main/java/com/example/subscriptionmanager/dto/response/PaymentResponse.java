package com.example.subscriptionmanager.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class PaymentResponse {
    private Integer id;
    private BigDecimal amount;
    private String currency;
    private OffsetDateTime paidAt;
    private Integer subscriptionId;
}
