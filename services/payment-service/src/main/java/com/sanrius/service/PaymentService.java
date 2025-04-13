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
import java.util.List;

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
            } else if ("no_payment_required".equalsIgnoreCase(paymentsStatus)) {
                // Maybe a free item
                log.info("This is a free item");
                break;
            }
            sleep();
        }


        log.info("Session: {}", updatedSession);

        // When gets here the payment will be successful,
        // that's why we should store the payment in the db,
        // but also add the userId that made the donation

        // 1. Request the userId of the user from the user-service -> request.getUserEmail()

        Payment payment = Payment.builder()
                .sessionId(updatedSession.getId())
                .paymentIntentId(updatedSession.getPaymentIntent())
                .amount(request.getAmount())
                .currency(updatedSession.getCurrency())
                .customerEmail(session.getCustomerEmail())
                 .customerEmail(updatedSession.getCustomerDetails().getEmail())
                .paymentStatus(PAID)
                .updatedAt(LocalDateTime.now())
                .build();

        log.info("Payment : {}", payment);
        paymentRepository.save(payment);
        log.info("Successfully saved the payment");
    }

    /**
     * Used by the <b>user-service</b> when fetching the info about the user donation-history
     * @param userId used to identify the user whose info should be returned
     * @return the donation-history of the user
     */
    public List<Payment> getUsersDonationHistory(Long userId) {
        List<Payment> donationList = paymentRepository.findAll().stream()
                .filter(payment -> payment.getUserId().equals(userId))
                .toList();

        log.info("The paymentList of the user with the ID {}; {}", userId, donationList);
        return donationList;
    }


}
