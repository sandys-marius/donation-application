package com.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@EnableMethodSecurity(securedEnabled = true)
public class TestController {

//    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public String testUser() {
        return "Hello user";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/admin")
    public String testAdmin() {
        return "Hello admin";
    }

//    @PreAuthorize("hasAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/anyone")
    public String testAnyone() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return "Hello anyone: " + auth.getName();
    }

}
