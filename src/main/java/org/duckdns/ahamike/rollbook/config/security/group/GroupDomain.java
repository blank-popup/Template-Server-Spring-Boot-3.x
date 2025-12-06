package org.duckdns.ahamike.rollbook.config.security.group;

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
public class GroupDomain extends BaseDomain {
    private String name;
    private String description;
}
