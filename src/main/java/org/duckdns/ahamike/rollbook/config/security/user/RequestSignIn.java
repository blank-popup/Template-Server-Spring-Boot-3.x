package org.duckdns.ahamike.rollbook.config.security.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestSignIn {
    private String username;
    private String password;
}
