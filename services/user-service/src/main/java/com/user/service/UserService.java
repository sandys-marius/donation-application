package com.user.service;

import com.google.gson.Gson;
import com.user.kafka.KafkaListeners;
import com.user.kafka.KafkaProducerService;
import com.user.model.User;
import com.user.repository.UserRepository;
import com.user.utils.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
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
