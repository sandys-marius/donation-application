package com.user.filter;

import com.user.service.JwtService;
import com.user.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        log.info("In the doFilterInternal of the {} | JwtAuthFilter.class", this.getClass().getName());

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
