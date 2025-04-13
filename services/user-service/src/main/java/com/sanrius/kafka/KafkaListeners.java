package com.sanrius.kafka;

import com.google.gson.Gson;
import com.sanrius.utils.Donation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class KafkaListeners {

    private final Gson gson = new Gson();
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaListeners(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "response-user-service-health-check", groupId = "groupId")
    public void getHealthResponse(String message) {
        log.info("Got a health response");
        log.info("Message: {}", message);
    }

    @KafkaListener(topics = "response-donation-history", groupId = "groupId")
    public void getUserDonationHistory(List<Donation> donationList) {
        log.info("Got a donation history response");
        log.info("Donation List: {}", donationList);

    }

}
