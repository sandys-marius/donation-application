package com.sanrius.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Long userId;
    private String userEmail;
    private String userPassword;
}
