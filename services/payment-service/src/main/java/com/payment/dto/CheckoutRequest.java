package com.payment.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CheckoutRequest {
    private Long amount;
    private String userEmail;
}
