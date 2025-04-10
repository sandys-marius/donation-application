package com.sanrius.controller;

import com.sanrius.dto.CheckoutRequest;
import com.sanrius.service.CheckoutService;
import com.stripe.Stripe;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/checkout")
public class CheckoutController {

    private final Dotenv dotenv = Dotenv.configure().directory("/home/simba/IdeaProjects/donation-application/services/payment-service").load();
    private final CheckoutService checkoutService;

    @PostConstruct
    public void initStripe() {
        // Set Stripe API key once
        Stripe.apiKey = dotenv.get("STRIPE_SECRET_KEY");
    }

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createCheckout(@RequestBody CheckoutRequest request) {
        return ResponseEntity.ok(checkoutService.createCheckout(request));
    }

}
