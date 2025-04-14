package com.sanrius.controllers;

import com.sanrius.dto.CreateUserRequest;
import com.sanrius.kafka.KafkaProducerService;
import com.sanrius.model.User;
import com.sanrius.services.UserService;
import com.sanrius.utils.Donation;
import com.sanrius.utils.Payment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<User> getUser(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @PostMapping()
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userService.deleteUser(userId));
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
