package org.duckdns.ahamike.rollbook.process.project.attendance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestAttend {
    private String tag;
    private String terminal;
}
