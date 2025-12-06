package org.duckdns.ahamike.rollbook.config.security;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import org.duckdns.ahamike.rollbook.config.redis.ServiceRedis;
import org.duckdns.ahamike.rollbook.util.crypto.Crypto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class ApiKeyProvider {
    private final UserDetailsService userDetailsService;
    @Value("${auth.apikey.key}")
    private String secretKey;
    @Value("${auth.apikey.iv}")
    private String secretIv;
    @Value("${auth.apikey.delimiter}")
    private String delimiter;

    private final ServiceRedis redisService;

    @Value("${spring.data.redis.group0_apikey}")
    private String group0_apikey;

    public String createApiKey(Long id, Long terms) {
        Date now = new Date();
        long nowMilliSeconds = now.getTime();
        String apiKeyInformation = id + delimiter + (nowMilliSeconds + terms);
        log.debug("Information of API Key : {}", apiKeyInformation);
        String hashInformation = Crypto.hashSHA256(apiKeyInformation);
        // hashInformation : 6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b
        log.debug("Hash of information : {}", hashInformation);
        String plainText = apiKeyInformation + delimiter + hashInformation;
        String encryptedText = Crypto.encryptAES(plainText, secretKey, secretIv);
        // API Key : mqn6nH0/6hJkOOVrJlzuVL9p8bxGPNySp4gKdTwT5hjD+pIWmwGBS5FJJG0o1Vg02tXq1T2ak+mWu7B5M5t13tUxBWQNtTuHoD2/pIpLc2Q=
        log.debug("API Key : {}", encryptedText);

        return encryptedText;
    }

    public Authentication getAuthentication(List<String> information, String apiKey) throws NullPointerException {
        log.info("API Key authentication : ID : {}", information.get(0));
        String usernameKey = redisService.buildKey(group0_apikey, information.get(0), apiKey);
        UserDetails userDetails = userDetailsService.loadUserByUsername(usernameKey);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String resolveApiKey(HttpServletRequest request) {
        String apiKey = request.getHeader("X-API-KEY");

        if (StringUtils.hasText(apiKey)) {
            return apiKey;
        }

        return null;
    }

    public List<String> getInformationOfApiKey(String apiKey) {
        try {
            String decryptedText = Crypto.decryptAES(apiKey, secretKey, secretIv);
            String apiKeyInformation = decryptedText.substring(0, decryptedText.lastIndexOf(delimiter));
            String hashInformation = decryptedText.substring(decryptedText.lastIndexOf(delimiter) + delimiter.length());
            String hash = Crypto.hashSHA256(apiKeyInformation);
            if (hash == null || hash.equals(hashInformation) == false) {
                log.warn("API Key : Invalid hash");
                return null;
            }

            List<String> decrypts = Arrays.asList(decryptedText.split(delimiter));
            if (decrypts.size() != 3) {
                log.warn("API Key : Invalid size of list");
                return null;
            }

            Date now = new Date();
            Long expireAt = Long.parseLong(decrypts.get(1));
            if (now.getTime() > expireAt) {
                log.warn("API Key : Current time : {}, Expired time : {}", now.getTime(), expireAt);
                return null;
            }

            return decrypts;
        } catch (IndexOutOfBoundsException e) {
            log.warn("{}", e.toString());
        } catch (PatternSyntaxException e) {
            log.warn("{}", e.toString());
        } catch (NumberFormatException e) {
            log.warn("{}", e.toString());
        }

        return null;
    }
}
