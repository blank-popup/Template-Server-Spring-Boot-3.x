package org.duckdns.ahamike.rollbook.config.security;

import org.duckdns.ahamike.rollbook.config.security.tenant.setting.TenantFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Profile({"testauth", "service"})
public class SecurityConfigService {

    private final JwtProvider jwtProvider;
    private final ApiKeyProvider apiKeyProvider;
    private final CorsConfigurationSource corsConfigurationSource;

    private final HandlerExceptionResolver handlerExceptionResolver;

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
                        // .requestMatchers("/swagger-ui.html").permitAll()
                        // .requestMatchers("/swagger-ui/**").permitAll()
                        // .requestMatchers("/openapi3.json").permitAll()
                        // .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.POST, signUpUri).permitAll()
                        .requestMatchers(HttpMethod.POST, signInUri).permitAll()
                        .requestMatchers("/error/**").permitAll()
                        .requestMatchers("/v1/admin/**").hasAnyRole("ADMIN")
                        // .requestMatchers("/v1/**").permitAll()
                        // .requestMatchers("/v1/user/**").hasAnyRole("ADMIN", "MANAGER")
                        // .requestMatchers("/v1/user/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_MANAGER")
                        .anyRequest().access(serviceAuthorization)
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authenticationException) ->
                            handlerExceptionResolver.resolveException(request, response, null, authenticationException)
                        )
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                            handlerExceptionResolver.resolveException(request, response, null, accessDeniedException)
                        )
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
