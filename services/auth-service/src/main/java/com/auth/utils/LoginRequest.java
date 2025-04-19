package com.auth.utils;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class LoginRequest {
    private String email;
    private String password;
}