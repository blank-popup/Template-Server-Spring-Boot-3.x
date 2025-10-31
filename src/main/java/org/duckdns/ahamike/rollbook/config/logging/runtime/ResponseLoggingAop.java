package org.duckdns.ahamike.rollbook.config.logging.runtime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseLoggingAop {
    private Boolean isLoggingRequest;
    private Boolean isLoggingResponse;
    private Boolean isLoggingDatabase;
}
