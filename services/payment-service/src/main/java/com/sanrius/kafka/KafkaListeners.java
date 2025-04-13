package com.sanrius.kafka;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class KafkaListeners {

    private final Gson gson = new Gson();
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaProducerService producerService;


    public KafkaListeners(KafkaTemplate<String, Object> kafkaTemplate, KafkaProducerService producerService) {
        this.kafkaTemplate = kafkaTemplate;
        this.producerService = producerService;
    }

    @KafkaListener(topics = "check-payment-service-health", groupId = "groupId")
    public void getHealthRequest(String testData) {
        log.info("Got a health check");
        log.info("Data: {}", testData);
        kafkaTemplate.send("response-user-service-health-check", "Everything is good");
    }

    @KafkaListener(topics = "request-donation-history", groupId = "groupId")
    public void getDonationHistoryRequest(String userId) {
        log.info("Got a donationHistoryRequest. Raw userId: {}", userId);
        Long parsedUserId = Long.parseLong(gson.fromJson(userId, String.class));
        log.info("Parsed userId: {}", parsedUserId);
        producerService.sendUserDonationHistory(parsedUserId);
    }

}
