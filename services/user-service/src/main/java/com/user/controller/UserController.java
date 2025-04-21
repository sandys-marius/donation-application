package com.user.controller;

import com.user.kafka.KafkaProducerService;
import com.user.model.User;
import com.user.service.UserService;
import com.user.utils.Payment;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
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
    public ResponseEntity<List<Payment>> getUsers(
            @PathVariable("userId") Integer userId,
            @RequestHeader("Authorization") String auth
    ) {
        return ResponseEntity.ok(userService.getUserDonationHistory(userId, auth));
    }

//    @GetMapping("/testRestTemplate")
//    public String testRestTemplate(
//            @RequestHeader("Authorization") String auth,
//            HttpServletRequest request
//    ) {
//        log.info("Auth: {}", auth);
//
//        String url = "http://auth-service/test";
//
//        HttpHeaders headers = new HttpHeaders();
//        String jwt = getJWT(auth);
//        headers.setBearerAuth(jwt);
//        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
//
//        ResponseEntity<String> response = restTemplate.exchange(
//                url,
//                HttpMethod.GET,
//                requestEntity,
//                String.class
//        );
//
//        log.info("Response: {}", response);
//        return response.getBody();
//    }

    private String getJWT(String authorization) {
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        } else {
            throw new RuntimeException("Invalid authorization header");
        }
    }

}
