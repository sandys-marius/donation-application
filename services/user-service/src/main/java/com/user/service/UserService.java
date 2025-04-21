package com.user.service;

import com.auth.exception.UserNotFoundException;
import com.auth.jwt.JwtUtils;
import com.google.gson.Gson;
import com.user.kafka.KafkaListeners;
import com.user.kafka.KafkaProducerService;
import com.user.model.User;
import com.user.repository.UserRepository;
import com.user.utils.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final Gson gson = new Gson();
    private final UserRepository userRepository;
    private final KafkaProducerService producerService;
    private final KafkaListeners listener;
    private final RestTemplate restTemplate;
    private final JwtService jwtService;

    public static void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public User getUser(Integer userId) {
        return handleUserRequest(userId);
    }

    private User handleUserRequest(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with the ID: " + userId));
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

//    public String deleteUser(Integer userId) {
//        User userToDelete = handleUserRequest(userId);
//        userRepository.delete(userToDelete);
//        return "Deleted successfully user with the ID: " + userId;
//    }



    /**
     * Method used in the payment-service when adding the payment in the db,
     * very important because this is how we could show the donation history of every user
     * @param userEmail the email of the user
     * @return ID of the user
     */
    public Integer getUserIdByEmail(Integer userEmail) {
        User user = handleUserRequest(userEmail);
        return user.getUserId();
    }

    public List<Payment> getUserDonationHistory(Integer userId, String auth) {
        String jsonToken = getJWT(auth);
        System.out.println("Token being sent: " + jsonToken);

        String email = jwtService.extractUsername(jsonToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new  UserNotFoundException("User not found. With the username: " + email));


        if (!Objects.equals(userId, user.getUserId())) {
            throw new RuntimeException("Invalid request. The searched history is from another user");
        }

        List<Payment> payments = new ArrayList<>();

        String url = "http://payment-service/payments/donation-history/" + user.getUserId();

        // 1. Make a request to the payment-service from which to get the donation-history
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jsonToken);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List<Payment>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<Payment>>() {}
        );

        payments = response.getBody();

        log.info("Got the payment history list in the service: {}", payments);
        return payments;
    }

    private String getJWT(String authorization) {
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        } else {
            throw new RuntimeException("Invalid authorization header");
        }
    }

}
