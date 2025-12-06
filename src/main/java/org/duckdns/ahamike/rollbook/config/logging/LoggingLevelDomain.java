package org.duckdns.ahamike.rollbook.config.logging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoggingLevelDomain {
    private String loggerName;
    private String level;
}
