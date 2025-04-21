package com.payment.controller;

import com.payment.dto.Payment;
import com.payment.service.JwtService;
import com.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final JwtService jwtService;

    @GetMapping("/donation-history/{userId}")
    public ResponseEntity<List<Payment>> getDonationHistory(
            @PathVariable("userId") Integer userId,
            @RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", "").trim();

        return new ResponseEntity<List<Payment>>(paymentService.getUsersDonationHistory(userId), HttpStatus.OK);
    }

}
