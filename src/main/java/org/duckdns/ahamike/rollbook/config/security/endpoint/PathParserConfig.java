package org.duckdns.ahamike.rollbook.config.security.endpoint;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.pattern.PathPatternParser;

@Configuration
public class PathParserConfig {
    @Bean
    public PathPatternParser pathPatternParser() {
        PathPatternParser parser = new PathPatternParser();
        return parser;
    }
}
