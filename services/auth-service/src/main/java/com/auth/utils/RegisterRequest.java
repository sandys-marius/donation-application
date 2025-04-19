package com.auth.utils;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RegisterRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String authorities;

}