package org.duckdns.ahamike.rollbook.config.security.user;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestSignUp {
    private String username;
    private String password;
    private String tag;
    private String name;
    private String email;
    private String phone;
    private Set<String> roles = new HashSet<>();
}
