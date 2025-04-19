package com.user.controllers;

import com.user.kafka.KafkaProducerService;
import com.user.model.User;
import com.user.service.UserService;
import com.user.utils.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final KafkaProducerService producerService;

    public UserController(UserService userService, KafkaProducerService producerService) {
        this.userService = userService;
        this.producerService = producerService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable("userId") Integer userId) {
        log.info("Requesting a user with the ID: {}", userId);
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }


    @GetMapping("/donation-history/{userId}")
    public ResponseEntity<List<Payment>> getUsers(@PathVariable("userId") String userId) {
        return ResponseEntity.ok(userService.getUserDonationHistory(userId));
    }

    @GetMapping("/test")
    public void testKafka() {
        producerService.checkPaymentServiceHealth();
    }
}
