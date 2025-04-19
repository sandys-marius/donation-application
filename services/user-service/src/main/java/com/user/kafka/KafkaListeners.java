package com.user.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.user.utils.Payment;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
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
    private final ObjectMapper objectMapper;

    @Autowired
    public KafkaListeners(KafkaTemplate<String, Object> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
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

    @KafkaListener(id = "qux", topicPattern = "myTopic1")
    public void listen(@Payload String foo,
                       @Header(name = KafkaHeaders.RECEIVED_KEY, required = false) Integer key,
                       @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                       @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                       @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long timeStamp
    ) {
        log.info("Listen the request with the topic: {}", topic);
        log.info("Payload: {}", foo);
        log.info("KafkaHeaders.RECEIVED_KEY: {}", key);
        log.info("KafkaHeaders.RECEIVED_TIMESTAMP: {}", timeStamp);
    }
}
