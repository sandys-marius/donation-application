package com.sanrius.kafka;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final Gson gson = new Gson();

    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void checkPaymentServiceHealth() {
        log.info("Checking if the PAYMENT-SERVICE is available");
        kafkaTemplate.send("check-payment-service-health", "USER-SERVICE");
    }

    public void requestUserDonationHistory(String userId)  {
        log.info("Requesting the donation-history for the user with the ID: {}", userId);
        kafkaTemplate.send("request-donation-history", userId);
    }
}
