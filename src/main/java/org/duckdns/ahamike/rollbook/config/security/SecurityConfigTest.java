package org.duckdns.ahamike.rollbook.config.security;

import org.duckdns.ahamike.rollbook.config.security.tenant.setting.TenantFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Profile("test")
public class SecurityConfigTest {
    private final JwtProvider jwtProvider;
    private final ApiKeyProvider apiKeyProvider;
    private final CorsConfigurationSource corsConfigurationSource;

    @Value("${auth.permitAll.signUp}")
    private String signUpUri;
    @Value("${auth.permitAll.signIn}")
    private String signInUri;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthorizationService serviceAuthorization) throws Exception {
        http
                .httpBasic(basic -> basic.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/**").permitAll()
                )
                .addFilterBefore(
                    new AuthenticationFilterCustom(jwtProvider, apiKeyProvider),
                    UsernamePasswordAuthenticationFilter.class
                )
                .addFilterBefore(
                    new TenantFilter(),
                    AuthenticationFilterCustom.class
                );
        return http.build();
    }
}
