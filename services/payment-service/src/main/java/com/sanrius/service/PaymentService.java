package com.sanrius.service;

import com.sanrius.dto.CheckoutRequest;
import com.sanrius.dto.Payment;
import com.sanrius.exception.NoCreditException;
import com.sanrius.repositories.PaymentRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.sanrius.utils.PaymentStatusEnum.PAID;

@Slf4j
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    static void sleep() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void addPayment(Session session, CheckoutRequest request) {
        // Put this here to make it accessible through the whole method
        Session updatedSession = null;
        while (true) {
            try {
                updatedSession = Session.retrieve(session.getId());
            } catch (StripeException e) {
                throw new RuntimeException(e);
            }


            String paymentsStatus = updatedSession.getPaymentStatus();
            log.info("PaymentStatus: {}", paymentsStatus);
            if (paymentsStatus.equalsIgnoreCase("paid")) {
                log.info("Session is paid");
                log.info("Continuing with the payment process");
                break;
            } else if (paymentsStatus.equalsIgnoreCase("unpaid")) {
                log.info("Unpaid Checkout");
                throw new NoCreditException("Not enough credits for the user with the email: " + updatedSession.getCustomerDetails().getEmail());
            } else if ("no_payment_required".equalsIgnoreCase(paymentsStatus)) {
                // Maybe a free item
                log.info("This is a free item");
                break;
            }
            sleep();
        }

        // Got that the ID and the customerEmail was [null],
        // because I was getting the info from the 'session' not 'updatedSession'
        // TODO: add the payment session in the db
        Payment payment = Payment.builder()
                .sessionId(updatedSession.getId())
                .paymentIntentId(updatedSession.getPaymentIntent())
                .amount(request.getAmount())
                .currency(updatedSession.getCurrency())
                .customerEmail(updatedSession.getCustomerDetails().getEmail())
                .paymentStatus(PAID)
                .updatedAt(LocalDateTime.now())
                .build();

        log.info("Payment : {}", payment);
        paymentRepository.save(payment);
        log.info("Successfully saved the payment");
    }
}
