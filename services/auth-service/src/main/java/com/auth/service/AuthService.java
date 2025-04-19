package com.auth.service;

import com.auth.exception.UserNotFoundException;
import com.auth.model.User;
import com.auth.repo.UserRepository;
import com.auth.utils.AuthResponse;
import com.auth.utils.LoginRequest;
import com.auth.utils.RegisterRequest;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Service
public class AuthService {

    // TODO: check the login method, it return a INTERNAL SERVER ERROR

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, JwtService jwtService, AuthenticationManager authenticationManager, RefreshTokenService refreshTokenService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
        this.passwordEncoder = passwordEncoder;
    }


    public ResponseEntity<AuthResponse> register(RegisterRequest request) {

        log.info("Starting the registering of a new user");

        String authorities = request.getAuthorities();
        // getting the user from the request
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // encoding the password
                .authorities(authorities)
                .isEnabled(true)
                .build();

        // saving it to the db
        User savedUser = userRepository.save(user);
        log.info("Saving the user in the DB");

        // creating the user's access token
        var accessToken = jwtService.generateToken(savedUser);
        log.info("Generating the access token");
        var refreshToken = refreshTokenService.createRefreshToken(savedUser.getEmail());
        log.info("Generating the refresh token");

        // returning the response with the access token attached
        return ResponseEntity.ok(AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .msg("Everything is perfect")
                .build());
    }

    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        log.info("Starting to login a user");

        User user = userRepository.findByEmail(loginRequest.getEmail())
             .orElseThrow(() -> new UserNotFoundException("User not found!"));

        // attempts to authenticate the passed Authentication object
        Authentication authentication = null;
        try {
             authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
            log.info("Authentication successful: {}", authentication);
        } catch (Exception e) {
            log.error("Authentication failed: {}", e.getMessage());
            return ResponseEntity.status(401).body(
                    AuthResponse.builder()
                            .msg(e.getMessage())
                            .build()
            );
        }

        log.info("Authentication: {}", authentication);


        var accessToken = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getEmail());

        return ResponseEntity.ok(AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .msg("Returning the auth response to the login method")
                .build());
    }

}