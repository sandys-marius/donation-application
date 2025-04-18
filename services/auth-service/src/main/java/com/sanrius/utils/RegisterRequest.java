package com.sanrius.utils;

import lombok.*;
import org.hibernate.sql.ast.tree.expression.Collation;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

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