package com.sanrius.controllers;

import com.sanrius.dto.CreateUserRequest;
import com.sanrius.model.User;
import com.sanrius.services.UserService;
import com.sanrius.utils.Donation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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
    public ResponseEntity<List<Donation>> getUsers(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userService.getUserDonationService(userId));
    }
}
