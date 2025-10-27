package org.duckdns.ahamike.rollbook.config.security.endpoint;

import java.util.List;

import org.springframework.web.util.pattern.PathPattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InfoEndpoint {
    private String name;
    private String method;
    private String path;
    private PathPattern pattern;
    private List<String> roles;
}
