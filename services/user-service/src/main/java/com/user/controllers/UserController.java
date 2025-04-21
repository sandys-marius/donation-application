package com.user.controllers;

import com.user.kafka.KafkaProducerService;
import com.user.model.User;
import com.user.service.UserService;
import com.user.utils.Payment;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final RestTemplate restTemplate;
    private final UserService userService;
    private final KafkaProducerService producerService;

    public UserController(RestTemplate restTemplate, UserService userService, KafkaProducerService producerService) {
        this.restTemplate = restTemplate;
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


    @GetMapping("/testRestTemplate")
    public void testRestTemplate(
            @RequestHeader("Authorization") String auth,
            HttpServletRequest request
    ) {
        log.info("Auth: {}", auth);
        log.info("AuthType: {}", request.getAuthType());
        log.info("RequestURI: {}", request.getRequestURI());
        log.info("RequestURL: {}", request.getRequestURL());
        log.info("ServletPath: {}", request.getServletPath());
        log.info("Remote Address: {}", request.getRemoteAddr());
        log.info("Request Headers: {}", request.getHeaderNames());



//        String url = "http://localhost:8030/api/v1/user/" + 4;
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(auth);

//        ResponseEntity<User> response = restTemplate.getForEntity (
//                url,
//                User.class
//        );
//        log.info("Response: {}", response);
    }
}
