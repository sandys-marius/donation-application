package com.sanrius.controller;

import com.sanrius.service.AuthService;
import com.sanrius.utils.AuthResponse;
import com.sanrius.utils.LoginRequest;
import com.sanrius.utils.RegisterRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authServiceImpl, AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {
        System.out.println("The register request content is: " + registerRequest);
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        // System.out.println(loginRequest);
        log.info("Received a login request");
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        log.info("Testing the ping request");
        return ResponseEntity.ok("pong");
    }


}