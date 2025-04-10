package com.sanrius.service;

import com.sanrius.dto.CheckoutRequest;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CheckoutService {

    private final PaymentService paymentService;

    public CheckoutService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public String createCheckout(CheckoutRequest request) {
        log.info("The checkout request is: {}", request);
        final Long finalPrice = request.getAmount() * 100;

        SessionCreateParams params =
                SessionCreateParams.builder()
                        .addLineItem(
                                SessionCreateParams.LineItem.builder()
                                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("usd")
                                                .setUnitAmount(finalPrice)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("DONATION")
                                                                .build())
                                                .build())
                                        .setQuantity(1L)
                                        .build())
                        .setSuccessUrl("https://example.com")
                        .setCancelUrl("https://cancel.com")
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .build();

        Session session = null;
        try {
             session = Session.create(params);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }

        Session finalSession = session;
        new Thread(() -> paymentService.addPayment(finalSession, request)).start();

        return session.getUrl();
    }
}
