package com.sanrius.kafka;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaListeners {

    private final Gson gson = new Gson();
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaProducerService producerService;



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

//    @KafkaListener(topics = "topic.request")
//    public void listen(ConsumerRecord<String, String> record) {
//        log.info("THE SERVICE B GOT THE REQUEST");
//        String correlationId = new String(record.headers().lastHeader("correlationId").value());
//        String replyTopic = new String(record.headers().lastHeader("replyTopic").value());
//
//        String response = process(record.value());
//
//        ProducerRecord<String, String> responseRecord = new ProducerRecord<>(replyTopic, response);
//        responseRecord.headers().add("correlationId", correlationId.getBytes());
//        testTemplate.send(responseRecord);
//        log.info("SENDING BACK THE REPLY");
//    }
//
//    private String process(String incomingMessage) {
//        // Simulate doing something with the message
//        System.out.println("Received message: " + incomingMessage);
//
//        // Example: Transform or look something up
//        return "Processed result for: " + incomingMessage;
//    }



}
