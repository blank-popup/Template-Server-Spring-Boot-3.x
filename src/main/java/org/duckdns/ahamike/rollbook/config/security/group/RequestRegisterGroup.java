package org.duckdns.ahamike.rollbook.config.security.group;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestRegisterGroup {
    private String name;
    private String description;
}
