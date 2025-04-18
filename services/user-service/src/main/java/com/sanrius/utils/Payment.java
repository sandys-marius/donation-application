package com.sanrius.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sanrius.utils.PaymentStatusEnum;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    private Long paymentId;
    private String sessionId;
    private String paymentIntentId;
    private Long amount;
    private String currency;
    private String customerEmail;
    private PaymentStatusEnum paymentStatus;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime updatedAt;
    private Long userId;
}
