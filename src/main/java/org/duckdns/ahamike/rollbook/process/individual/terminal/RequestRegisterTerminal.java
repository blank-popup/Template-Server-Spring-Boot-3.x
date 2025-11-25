package org.duckdns.ahamike.rollbook.process.individual.terminal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestRegisterTerminal {
    private String name;
    private String description;
}
