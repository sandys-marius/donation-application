package com.sanrius.kafka;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final Gson gson = new Gson();


    public void checkPaymentServiceHealth() {
        log.info("Checking if the PAYMENT-SERVICE is available");
        kafkaTemplate.send("check-payment-service-health", "USER-SERVICE");
    }

    public void requestUserDonationHistory(String userId)  {
        log.info("Requesting the donation-history for the user with the ID: {}", userId);
        kafkaTemplate.send("request-donation-history", userId);
    }

//    Map<String, CompletableFuture<String>> responseFutures = new ConcurrentHashMap<>();
//
//    public void sendTestRequest() {
//        String correlationId = UUID.randomUUID().toString();
//        ProducerRecord<String, String> record = new ProducerRecord<>("topic.request", "TEST MESSAGE");
//        record.headers().add("correlationId", correlationId.getBytes());
//        record.headers().add("replyTopic", "topic.reply".getBytes());
//
//        testTemplate.send(record);
//
//        // When you send the message:
//        CompletableFuture<String> future = new CompletableFuture<>();
//        responseFutures.put(correlationId, future);
//        try {
//            String reply = future.get(10, TimeUnit.SECONDS); // Wait max 10 seconds
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @KafkaListener(topics = "topic.reply")
//    public void handleReply(ConsumerRecord<String, String> record) {
//        log.info("HANDLING THE TEST REPLY");
//        String correlationId = new String(record.headers().lastHeader("correlationId").value());
//        CompletableFuture<String> future = responseFutures.remove(correlationId);
//        if (future != null) {
//            future.complete(record.value());
//        }
//    }


}
