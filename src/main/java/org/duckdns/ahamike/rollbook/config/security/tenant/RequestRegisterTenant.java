package org.duckdns.ahamike.rollbook.config.security.tenant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestRegisterTenant {
    private String belong;
    private String name;
    private String description;
    
}
