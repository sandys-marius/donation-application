package com.sanrius.kafka;

import com.sanrius.dto.Payment;
import com.sanrius.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final PaymentService paymentService;

    public void sendUserDonationHistory(Long userId) {
        List<Payment> usersDonationHistory = paymentService.getUsersDonationHistory(userId);
        kafkaTemplate.send("response-donation-history", usersDonationHistory);
    }
}
