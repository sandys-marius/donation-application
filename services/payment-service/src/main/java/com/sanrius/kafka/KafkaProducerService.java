package com.sanrius.kafka;

import com.google.gson.Gson;
import com.sanrius.dto.Payment;
import com.sanrius.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final PaymentService paymentService;
    private final Gson gson = new Gson();

    public void sendUserDonationHistory(Long userId) {
        List<Payment> usersDonationHistory = paymentService.getUsersDonationHistory(userId);
        log.info("Sending user donation history");
        kafkaTemplate.send("response-donation-history", usersDonationHistory);
    }
}
