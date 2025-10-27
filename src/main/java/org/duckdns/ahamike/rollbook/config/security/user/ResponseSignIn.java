package org.duckdns.ahamike.rollbook.config.security.user;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseSignIn {
    private Long id;
    private String username;
    private String token;
    private String name;
    private String email;
    private String phone;
    private String tag;
    private List<String> roles = new ArrayList<>();
}
