package org.duckdns.ahamike.rollbook.config.security.tenant;

import org.duckdns.ahamike.rollbook.config.autitable.BaseDomain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class TenantDomain extends BaseDomain {
    private String belong;
    private String name;
    private String description;
    
}
