package com.sanrius.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.sanrius.utils.Donation;
import com.sanrius.utils.Payment;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Getter
@Setter
@Component
public class KafkaListeners {

    List<Payment> paymentList;

    private final Gson gson = new Gson();
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;


    public KafkaListeners(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "response-user-service-health-check", groupId = "groupId")
    public void getHealthResponse(String message) {
        log.info("Got a health response");
        log.info("Message: {}", message);
    }

    @KafkaListener(topics = "response-donation-history", groupId = "groupId")
    public void getUserDonationHistory(String jsonResponse) {
        log.info("Got donation history response: {}", jsonResponse);
        try {
            List<Payment> payments = objectMapper.readValue(
                    jsonResponse,
                    new TypeReference<List<Payment>>() {}
            );
            this.paymentList = payments;
            log.info("Parsed Payment List: {}", payments);
            this.paymentList = payments;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
