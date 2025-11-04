package org.duckdns.ahamike.rollbook.config.security;

import java.util.Base64;
import java.util.Date;
import java.util.Set;

import org.duckdns.ahamike.rollbook.config.redis.ServiceRedis;
import org.duckdns.ahamike.rollbook.util.client.ClientInfo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ClaimsBuilder;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class ProviderJwt implements InitializingBean {
    @Value("${auth.jwt.key}")
    private String secretKey;
    @Value("${auth.jwt.expiration-access}")
    private long tokenValidMillisecond;
    private final UserDetailsService userDetailsService;
    private final ServiceRedis serviceRedis;

    @Value("${spring.data.redis.group0_user}")
    private String group0_user;

    @Override
    public void afterPropertiesSet() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createJwt(String username, Set<String> roles, String ip, String userAgent) {
        ClaimsBuilder claimsBuilder = Jwts.claims().subject(username);
        claimsBuilder.add("roles", roles);
        claimsBuilder.add("ip", ip);
        claimsBuilder.add("userAgent", userAgent);
        Claims claims = claimsBuilder.build();
        for (String key : claims.keySet()) {
            log.info("claims : [{}] : [{}]", key, claims.get(key));
        }
        Date now = new Date();

        String token = Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + tokenValidMillisecond))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), Jwts.SIG.HS512)
                .compact();
        log.info("Token : {}", token);

        return token;
    }

    @Transactional
    public Authentication getAuthentication(Jws<Claims> information, String token) {
        log.info("JWT authentication : usename : {}", information.getPayload().getSubject());
        String usernameKey = serviceRedis.buildKey(group0_user, information.getPayload().getSubject(), token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(usernameKey);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String resolveJwt(HttpServletRequest request) {
        String valueAuthorization = request.getHeader("Authorization");

        if (StringUtils.hasText(valueAuthorization) && valueAuthorization.startsWith("Bearer ")) {
            return valueAuthorization.substring(7);
        }

        return null;
    }

    public Jws<Claims> getInformationOfJwt(String jwt) {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            String remoteIp = ClientInfo.getRemoteIP(request);
            String remoteUserAgent = request.getHeader("User-Agent");

            // Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt);
            Jws<Claims> claims = Jwts
                    .parser()
                    .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseSignedClaims(jwt);
            String jwtIp = (String) claims.getPayload().get("ip");
            String jwtUserAgent = (String) claims.getPayload().get("userAgent");
            log.debug("Validate Token ipRemote : [{}], ipJwt : [{}]", remoteIp, jwtIp);
            log.debug("Validate Token userAgentRemote : [{}], userAgentJwt : [{}]", remoteUserAgent, jwtUserAgent);

            if (remoteIp == null || jwtIp == null || (remoteIp.equals(jwtIp) == false)) {
                log.warn("JWT : Invalid remote IP");
                return null;
            }
            if (remoteUserAgent == null || jwtUserAgent == null || (remoteUserAgent.equals(jwtUserAgent) == false)) {
                log.warn("JWT : Invalid User-Agent");
                return null;
            }
            if (claims.getPayload().getExpiration().before(new Date()) == true) {
                log.warn("JWT : Invalid terms");
                return null;
            }

            return claims;
        } catch (MalformedJwtException | IllegalArgumentException exception) {
            log.warn("Invalid JWT");
        } catch (ExpiredJwtException exception) {
            log.warn("Expired JWT");
        } catch (UnsupportedJwtException exception) {
            log.warn("Not supported JWT");
        }

        return null;
    }
}
