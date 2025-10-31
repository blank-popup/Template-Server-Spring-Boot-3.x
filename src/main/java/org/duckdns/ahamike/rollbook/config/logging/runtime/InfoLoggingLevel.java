package org.duckdns.ahamike.rollbook.config.logging.runtime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InfoLoggingLevel {
    private String loggerName;
    private String level;
}
