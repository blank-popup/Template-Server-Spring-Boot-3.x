package org.duckdns.ahamike.rollbook.config.security.role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestRegisterRole {
    private String name;
    private String description;
}
