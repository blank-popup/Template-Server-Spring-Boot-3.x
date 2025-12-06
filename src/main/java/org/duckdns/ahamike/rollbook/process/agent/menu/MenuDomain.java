package org.duckdns.ahamike.rollbook.process.agent.menu;

import java.util.Set;

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
public class MenuDomain extends BaseDomain {
    private Long parentId;
    private String name;    
    private String description;
    private Integer ordering;
    private Set<String> roles;
}
