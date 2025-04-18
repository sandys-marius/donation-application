package com.sanrius.service;

import com.sanrius.exception.UserNotFoundException;
import com.sanrius.model.User;
import com.sanrius.repo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bcrypt;

    // WORKS
    public ResponseEntity<User> getUser(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return ResponseEntity.ok(user);
    }

    public User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public String deleteUser(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        userRepository.deleteById(userId);
        log.info("User deleted with the id: {}", userId);
        return "User deleted with the id: " + userId;
    }

    // EMAIL & VALIDATION WORKS
    public User updateUserBig(int userId, String email, String firstName, String lastName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));


//        if (email != null){
//            userValidation.validateEmail(email);
//            user.setEmail(email);
//            userRepository.save(user);
//            log.debug("Email updated to: {} ", email);
//        }
//        if (firstName != null){
//            userValidation.validateFirstName(firstName);
//            user.setFirstName(firstName);
//            userRepository.save(user);
//            log.debug("FirstName updated to: {} ", firstName);
//        }
//        if (lastName != null){
//            userValidation.validateLastName(lastName);
//            user.setLastName(lastName);
//            log.debug("LastName updated to: {} ", lastName);
//        }
        log.debug("The new user is: {} ", user);
        log.info("User updated successfully");
        return user;
    }

    @Transactional
    public User updateUser(int userId, User user) {

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        String newEmail = user.getEmail();
        String newFirstName = user.getFirstName();
        String newLastName = user.getLastName();
//        if (newEmail != null){
//            userValidation.validateEmail(newEmail);
//            existingUser.setEmail(newEmail);
//            log.debug("Email updated to: {} ", newEmail);
//        }
//        if (newFirstName != null){
//            userValidation.validateFirstName(newFirstName);
//            existingUser.setFirstName(newFirstName);
//            log.debug("FirstName updated to: {} ", newFirstName);
//        }
//        if (newLastName != null){
//            userValidation.validateLastName(newLastName);
//            existingUser.setLastName(newLastName);
//            log. debug("LastName updated to: {} ", newLastName);
//
//        }
        log.debug("The new user is: {} ", existingUser);
        log.info("User updated successfully");
        return existingUser;
    }

    public Boolean verifyPassword(int userId, String password) {
        log.info("The user ID is: {}", userId);
        log.info("The password is: {}", password);
        String subPassword = password.substring(0, password.length() - 1);
        log.info("The new sub password is: {}", subPassword);
        User user =  userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with the id: " + userId));

        return bcrypt.matches(subPassword, user.getPassword());
    }


}