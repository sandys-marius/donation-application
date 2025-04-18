package com.sanrius.filter;

import java.io.IOException;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.sanrius.jwt.JwtUtils;
import com.sanrius.model.User;
import com.sanrius.service.JwtService;
import com.sanrius.service.UserDetailsServiceImpl;

import java.util.ArrayList;
import java.util.List;

import com.sanrius.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@AllArgsConstructor
@Configuration
public class AuthTokenFilter extends OncePerRequestFilter {

    private JwtUtils jwtUtils;
    private JwtService jwtService;
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        log.info("In the doFilterInternal of the {} | AuthTokenFilter.class", this.getClass().getName());

        try {
            String jwt = parseJwt(request);
            log.info("JWT: {}", jwt);

            if (jwt != null && jwtService.isTokenValid(jwt)) {
                log.info("The JWT is valid");

                // String username = jwtUtils.getUserNameFromJwtToken(jwt);
                String username = jwtService.extractUsername(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                log.info("UserDetails: {}", userDetails.getAuthorities());

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("Authentication set for user: {}", username);
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e.getMessage());
        }
        log.info("No error was thrown during the AuthTokenFilter");
        log.info("The authentication process continues...");

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        log.info("Parsing the JWT");
        log.info("The request: {}", request);
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}