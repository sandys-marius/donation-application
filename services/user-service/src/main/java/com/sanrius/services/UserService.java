package com.sanrius.services;

import com.google.gson.Gson;
import com.sanrius.dto.CreateUserRequest;
import com.sanrius.kafka.KafkaListeners;
import com.sanrius.kafka.KafkaProducerService;
import com.sanrius.model.User;
import com.sanrius.repositories.UserRepository;
import com.sanrius.utils.Donation;
import com.sanrius.utils.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserService {

    private final Gson gson = new Gson();
    private final UserRepository userRepository;
    private final KafkaProducerService producerService;
    private final KafkaListeners listener;

    public static void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public UserService(UserRepository userRepository, KafkaProducerService producerService, KafkaListeners listener) {
        this.userRepository = userRepository;
        this.producerService = producerService;
        this.listener = listener;
    }

    public User getUser(Long userId) {
        return handleUserRequest(userId);
    }

    private User handleUserRequest(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with the ID: " + userId));
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public String deleteUser(Long userId) {
        User userToDelete = handleUserRequest(userId);
        userRepository.delete(userToDelete);
        return "Deleted successfully user with the ID: " + userId;
    }

    public User createUser(CreateUserRequest request) {
        log.info("Request: {}", request);
        log.info("Preparing for the creation of the user");
        User user = User.builder()
                .userEmail(request.getUserEmail())
                .userPassword(request.getUserPassword())
                .build();
        log.info("User: {}", user);
        return userRepository.save(user);
    }

    /**
     * Method used in the payment-service when adding the payment in the db,
     * very important because this is how we could show the donation history of every user
     * @param userEmail the email of the user
     * @return ID of the user
     */
    public Long getUserIdByEmail(Long userEmail) {
        User user = handleUserRequest(userEmail);
        return user.getUserId();
    }

    public List<Payment> getUserDonationHistory(String userId) {
        // 1. Make a request to the payment-service from which to get the donation-history
        producerService.requestUserDonationHistory(userId);

        List<Payment> payments = null;
        do {
            sleep();
            payments = listener.getPaymentList();
        } while (payments == null);

        log.info("Got the payment history list in the service: {}", payments);
        return payments;
    }

}
