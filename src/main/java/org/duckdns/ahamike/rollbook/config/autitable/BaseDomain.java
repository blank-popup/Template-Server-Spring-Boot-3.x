package org.duckdns.ahamike.rollbook.config.autitable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class BaseDomain {
    protected Long id;
    protected Boolean isEnabled;
}
